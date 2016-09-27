angular.module("ui.rCalendar.tpls", ["template/rcalendar/calendar.html", "template/rcalendar/week.html"]);
angular.module('ui.rCalendar', ['ui.rCalendar.tpls'])
    .constant('calendarConfig', {
        formatDay: 'dd',
        formatDayHeader: 'EEE',
        formatDayTitle: 'MMMM dd, yyyy',
        formatWeekTitle: 'MMMM yyyy, w',
        formatMonthTitle: 'MMMM yyyy',
        formatWeekViewDayHeader: 'EEE d',
        formatHourColumn: 'ha',
        calendarMode: 'month',
        showWeeks: false,
        showEventDetail: true,
        startingDay: 0,
        eventSource: null,
        queryMode: 'local'
    })
    .controller('ui.rCalendar.CalendarController', ['$scope', '$attrs', '$parse', '$interpolate', '$log', 'dateFilter', 'calendarConfig', function ($scope, $attrs, $parse, $interpolate, $log, dateFilter, calendarConfig) {
        'use strict';
        var self = this,
            ngModelCtrl = {$setViewValue: angular.noop}; // nullModelCtrl;

        // Configuration attributes
        angular.forEach(['formatDay', 'formatDayHeader', 'formatDayTitle', 'formatWeekTitle', 'formatMonthTitle', 'formatWeekViewDayHeader', 'formatHourColumn',
            'showWeeks', 'showEventDetail', 'startingDay', 'eventSource', 'queryMode'], function (key, index) {
            self[key] = angular.isDefined($attrs[key]) ? (index < 7 ? $interpolate($attrs[key])($scope.$parent) : $scope.$parent.$eval($attrs[key])) : calendarConfig[key];
        });

        $scope.$parent.$watch($attrs.eventSource, function (value) {
            self.onEventSourceChanged(value);
        });

        $scope.calendarMode = $scope.calendarMode || calendarConfig.calendarMode;
        if (angular.isDefined($attrs.initDate)) {
            self.currentCalendarDate = $scope.$parent.$eval($attrs.initDate);
        }
        if (!self.currentCalendarDate) {
            self.currentCalendarDate = new Date();
            if ($attrs.ngModel && !$scope.$parent.$eval($attrs.ngModel)) {
                $parse($attrs.ngModel).assign($scope.$parent, self.currentCalendarDate);
            }
        }

        self.init = function (ngModelCtrl_) {
            ngModelCtrl = ngModelCtrl_;

            ngModelCtrl.$render = function () {
                self.render();
            };
        };

        self.render = function () {
            if (ngModelCtrl.$modelValue) {
                var date = new Date(ngModelCtrl.$modelValue),
                    isValid = !isNaN(date);

                if (isValid) {
                    this.currentCalendarDate = date;
                } else {
                    $log.error('"ng-model" value must be a Date object, a number of milliseconds since 01.01.1970 or a string representing an RFC2822 or ISO 8601 date.');
                }
                ngModelCtrl.$setValidity('date', isValid);
            }
            this.refreshView();
        };

        self.refreshView = function () {
            if (this.mode) {
                this.range = this._getRange(this.currentCalendarDate);
                this._refreshView();
                this.rangeChanged();
            }
        };

        // Split array into smaller arrays
        self.split = function (arr, size) {
            var arrays = [];
            while (arr.length > 0) {
                arrays.push(arr.splice(0, size));
            }
            return arrays;
        };

        self.onEventSourceChanged = function (value) {
            self.eventSource = value;
            if (self._onDataLoaded) {
                self._onDataLoaded();
            }
        };

        $scope.move = function (direction) {
            var step = self.mode.step,
                currentCalendarDate = self.currentCalendarDate,
                year = currentCalendarDate.getFullYear() + direction * (step.years || 0),
                month = currentCalendarDate.getMonth() + direction * (step.months || 0),
                date = currentCalendarDate.getDate() + direction * (step.days || 0),
                firstDayInNextMonth;

            currentCalendarDate.setFullYear(year, month, date);
            if ($scope.calendarMode === 'month') {
                firstDayInNextMonth = new Date(year, month + 1, 1);
                if (firstDayInNextMonth.getTime() <= currentCalendarDate.getTime()) {
                    self.currentCalendarDate = new Date(firstDayInNextMonth - 24 * 60 * 60 * 1000);
                }
            }
            ngModelCtrl.$setViewValue(self.currentCalendarDate);
            self.refreshView();
        };

        self.move = function (direction) {
            $scope.move(direction);
        };

        self.rangeChanged = function () {
            if (self.queryMode === 'local') {
                if (self.eventSource && self._onDataLoaded) {
                    self._onDataLoaded();
                }
            } else if (self.queryMode === 'remote') {
                if ($scope.rangeChanged) {
                    $scope.rangeChanged({
                        startTime: this.range.startTime,
                        endTime: this.range.endTime
                    });
                }
            }
        };

        function overlap(event1, event2) {
            return !(event1.endIndex <= event2.startIndex || event2.endIndex <= event1.startIndex);
        }

        function calculatePosition(events) {
            var i,
                j,
                len = events.length,
                maxColumn = 0,
                col,
                isForbidden = new Array(len);

            for (i = 0; i < len; i += 1) {
                for (col = 0; col < maxColumn; col += 1) {
                    isForbidden[col] = false;
                }
                for (j = 0; j < i; j += 1) {
                    if (overlap(events[i], events[j])) {
                        isForbidden[events[j].position] = true;
                    }
                }
                for (col = 0; col < maxColumn; col += 1) {
                    if (!isForbidden[col]) {
                        break;
                    }
                }
                if (col < maxColumn) {
                    events[i].position = col;
                } else {
                    events[i].position = maxColumn++;
                }
            }
        }

        function calculateWidth(orderedEvents) {
            var cells = new Array(24),
                event,
                index,
                i,
                j,
                len,
                eventCountInCell,
                currentEventInCell;

            //sort by position in descending order, the right most columns should be calculated first
            orderedEvents.sort(function (eventA, eventB) {
                return eventB.position - eventA.position;
            });
            for (i = 0; i < 24; i += 1) {
                cells[i] = {
                    calculated: false,
                    events: []
                };
            }
            len = orderedEvents.length;
            for (i = 0; i < len; i += 1) {
                event = orderedEvents[i];
                index = event.startIndex;
                while (index < event.endIndex) {
                    cells[index].events.push(event);
                    index += 1;
                }
            }

            i = 0;
            while (i < len) {
                event = orderedEvents[i];
                if (!event.overlapNumber) {
                    var overlapNumber = event.position + 1;
                    event.overlapNumber = overlapNumber;
                    var eventQueue = [event];
                    while ((event = eventQueue.shift())) {
                        index = event.startIndex;
                        while (index < event.endIndex) {
                            if (!cells[index].calculated) {
                                cells[index].calculated = true;
                                if (cells[index].events) {
                                    eventCountInCell = cells[index].events.length;
                                    for (j = 0; j < eventCountInCell; j += 1) {
                                        currentEventInCell = cells[index].events[j];
                                        if (!currentEventInCell.overlapNumber) {
                                            currentEventInCell.overlapNumber = overlapNumber;
                                            eventQueue.push(currentEventInCell);
                                        }
                                    }
                                }
                            }
                            index += 1;
                        }
                    }
                }
                i += 1;
            }
        }

        self.placeEvents = function (orderedEvents) {
            calculatePosition(orderedEvents);
            calculateWidth(orderedEvents);
        };

        self.placeAllDayEvents = function (orderedEvents) {
            calculatePosition(orderedEvents);
        };
    }])
    .directive('calendar', function () {
        'use strict';
        return {
            restrict: 'EA',
            replace: true,
            templateUrl: 'template/rcalendar/calendar.html',
            scope: {
                calendarMode: '=',
                rangeChanged: '&',
                eventSelected: '&',
                timeSelected: '&'
            },
            require: ['calendar', '?^ngModel'],
            controller: 'ui.rCalendar.CalendarController',
            link: function (scope, element, attrs, ctrls) {
                var calendarCtrl = ctrls[0], ngModelCtrl = ctrls[1];

                if (ngModelCtrl) {
                    calendarCtrl.init(ngModelCtrl);
                }

                scope.$on('changeDate', function (event, direction) {
                    calendarCtrl.move(direction);
                });

                scope.$on('eventSourceChanged', function (event, value) {
                    calendarCtrl.onEventSourceChanged(value);
                });
            }
        };
    })
    .directive('weekview', ['dateFilter', '$timeout', function (dateFilter, $timeout) {
        'use strict';
        return {
            restrict: 'EA',
            replace: true,
            templateUrl: 'template/rcalendar/week.html',
            require: '^calendar',
            link: function (scope, element, attrs, ctrl) {
                scope.formatWeekViewDayHeader = ctrl.formatWeekViewDayHeader;
                scope.formatHourColumn = ctrl.formatHourColumn;

                ctrl.mode = {
                    step: {days: 7}
                };

                function updateScrollGutter() {
                    var children = element.children();
                    var normalEventBody = children[1];
                    var normalEventGutterWidth = normalEventBody.offsetWidth - normalEventBody.clientWidth;
                    var gutterWidth = normalEventGutterWidth || 0;
                    if (gutterWidth > 0) {
                        scope.gutterWidth = gutterWidth;
                        if (normalEventGutterWidth <= 0) {
                            scope.normalGutterWidth = gutterWidth;
                        } else {
                            scope.normalGutterWidth = 0;
                        }
                    }
                }

               function getDates(startTime, n) {
                    var dates = new Array(n),
                        current = new Date(startTime),
                        i = 0;
                    current.setHours(12); // Prevent repeated dates because of timezone bug
                    while (i < n) {
                        dates[i++] = {
                            date: new Date(current)
                        };
                        current.setDate(current.getDate() + 1);
                    }
                    return dates;
                }

                function createDateObjects(startTime) {
                    var times = [],
                        row,
                        time,
                        /*Cambiar horas*/
                        currentHour = 9,
                        currentDate = startTime.getDate();
/*Cambiar horas*/   var cont = 0;
                    for (var hour = 0; hour < 9; hour += 1) {
                        row = [];
                        for (var day = 0; day < 7; day += 1) {
                            cont++;
                            time = new Date(startTime.getTime());
                            time.setHours(currentHour + hour);
                            time.setDate(currentDate + day);
                            row.push({
                                time: time,
                                index: cont
                            });
                        }
                        times.push(row);
                    }
                    return times;
                }

                scope.select = function (selectedTime, index) {
                    scope.selectedCell = index;
                    if (scope.timeSelected) {
                        scope.timeSelected({selectedTime: selectedTime});
                    }
                };

                ctrl._onDataLoaded = function () {
                    var eventSource = ctrl.eventSource,
                        len = eventSource ? eventSource.length : 0,
                        startTime = ctrl.range.startTime,
                        endTime = ctrl.range.endTime,
                        timeZoneOffset = -new Date().getTimezoneOffset(),
                        utcStartTime = new Date(startTime.getTime() + timeZoneOffset * 60 * 1000),
                        utcEndTime = new Date(endTime.getTime() + timeZoneOffset * 60 * 1000),
                        rows = scope.rows,
                        dates = scope.dates,
                        oneHour = 3600000,
                        oneDay = 86400000,
                    //add allday eps
                        eps = 0.016,
                        eventSet,
                        allDayEventInRange = false,
                        normalEventInRange = false,
                        day,
                        hour;

                    if (rows.hasEvent) {
                        for (day = 0; day < 7; day += 1) {
                            for (hour = 0; hour < 9; hour += 1) {
                                if (rows[hour][day].events) {
                                    rows[hour][day].events = null;
                                }
                            }
                        }
                        rows.hasEvent = false;
                    }

                    if (dates.hasEvent) {
                        for (day = 0; day < 7; day += 1) {
                            if (dates[day].events) {
                                dates[day].events = null;
                            }
                        }
                        dates.hasEvent = false;
                    }

                    for (var i = 0; i < len; i += 1) {
                        var event = eventSource[i];
                        var eventStartTime = new Date(event.startTime);
                        var eventEndTime = new Date(event.endTime);

                        if (event.allDay) {
                            if (eventEndTime <= utcStartTime || eventStartTime >= utcEndTime) {
                                continue;
                            } else {
                                allDayEventInRange = true;

                                var allDayStartIndex;
                                if (eventStartTime <= utcStartTime) {
                                    allDayStartIndex = 0;
                                } else {
                                    allDayStartIndex = Math.floor((eventStartTime - utcStartTime) / oneDay);
                                }

                                var allDayEndIndex;
                                if (eventEndTime >= utcEndTime) {
                                    allDayEndIndex = Math.ceil((utcEndTime - utcStartTime) / oneDay);
                                } else {
                                    allDayEndIndex = Math.ceil((eventEndTime - utcStartTime) / oneDay);
                                }

                                var displayAllDayEvent = {
                                    event: event,
                                    startIndex: allDayStartIndex,
                                    endIndex: allDayEndIndex
                                };

                                eventSet = dates[allDayStartIndex].events;
                                if (eventSet) {
                                    eventSet.push(displayAllDayEvent);
                                } else {
                                    eventSet = [];
                                    eventSet.push(displayAllDayEvent);
                                    dates[allDayStartIndex].events = eventSet;
                                }
                            }
                        } else {
                            if (eventEndTime <= startTime || eventStartTime >= endTime) {
                                continue;
                            } else {
                                normalEventInRange = true;

                                var timeDifferenceStart;
                                if (eventStartTime <= startTime) {
                                    timeDifferenceStart = 0;
                                } else {
                                    timeDifferenceStart = (eventStartTime - startTime) / oneHour;
                                }                                

                                var timeDifferenceEnd;
                                if (eventEndTime >= endTime) {
                                    timeDifferenceEnd = (endTime - startTime) / oneHour;
                                } else {
                                    timeDifferenceEnd = (eventEndTime - startTime) / oneHour;
                                }

                                var startIndex = Math.floor(timeDifferenceStart);
                                var endIndex = Math.ceil((timeDifferenceEnd-9) - eps);
                                var startRowIndex = startIndex % 3;
                                var dayIndex = Math.floor(startIndex / 24);
                                var endOfDay = dayIndex * 24;
                                var endRowIndex;

                                do {
                                    endOfDay += 24;
                                    if (endOfDay <= endIndex) {
                                        endRowIndex = 24;
                                    } else {
                                        endRowIndex = endIndex % 24;
                                    }
                                    var displayEvent = {
                                        event: event,
                                        startIndex: startRowIndex,
                                        endIndex: endRowIndex
                                    };
                                    eventSet = rows[startRowIndex][dayIndex].events;
                                    if (eventSet) {
                                        eventSet.push(displayEvent);
                                    } else {
                                        eventSet = [];
                                        eventSet.push(displayEvent);
                                        rows[startRowIndex][dayIndex].events = eventSet;
                                    }
                                    startRowIndex = 0;
                                    dayIndex += 1;
                                } while (endOfDay < endIndex);
                            }
                        }
                    }

                    if (normalEventInRange) {
                        for (day = 0; day < 7; day += 1) {
                            var orderedEvents = [];
                            for (hour = 0; hour < 9; hour += 1) {
                                if (rows[hour][day].events) {
                                    orderedEvents = orderedEvents.concat(rows[hour][day].events);
                                }
                            }
                            if (orderedEvents.length > 0) {
                                rows.hasEvent = true;
                                ctrl.placeEvents(orderedEvents);
                            }
                        }
                    }                   

                    $timeout(function () {
                        updateScrollGutter();
                    });
                };

                ctrl._refreshView = function () {
                    var firstDayOfWeek = ctrl.range.startTime,
                        dates = getDates(firstDayOfWeek, 7),
                        weekNumberIndex,
                        weekFormatPattern = 'w',
                        title;
                    scope.rows = createDateObjects(firstDayOfWeek);
                    scope.dates = dates;
                    weekNumberIndex = ctrl.formatWeekTitle.indexOf(weekFormatPattern);
                    title = dateFilter(firstDayOfWeek, ctrl.formatWeekTitle);
                    if (weekNumberIndex !== -1) {
                        title = title.replace(weekFormatPattern, getISO8601WeekNumber(firstDayOfWeek));
                    }
                    title = title.replace(',', ", Semana");
                    scope.$parent.title = title;
                };

                ctrl._getRange = function getRange(currentDate) {
                    var year = currentDate.getFullYear(),
                        month = currentDate.getMonth(),
                        date = currentDate.getDate(),
                        day = currentDate.getDay(),
                        firstDayOfWeek = new Date(year, month, date - day),
                        endTime = new Date(year, month, date - day + 7);

                    return {
                        startTime: firstDayOfWeek,
                        endTime: endTime
                    };
                };

                //This can be decomissioned when upgrade to Angular 1.3
                function getISO8601WeekNumber(date) {
                    var checkDate = new Date(date);
                    checkDate.setDate(checkDate.getDate() + 4 - (checkDate.getDay() || 7)); // Thursday
                    var time = checkDate.getTime();
                    checkDate.setMonth(0); // Compare with Jan 1
                    checkDate.setDate(1);
                    return Math.floor(Math.round((time - checkDate) / 86400000) / 7) + 1;
                }

                ctrl.refreshView();
            }
        }; 
    }]);
angular.module("template/rcalendar/calendar.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/rcalendar/calendar.html",
    "<div ng-switch=\"calendarMode\">\n" +
    "    <div class=\"row \">\n" +
    "        <div class=\"col s4 m2 l2\">\n" +
    "            <a tooltipped  data-position=\"top\" data-delay=\"500\" data-tooltip=\"Semana anterior\" type=\"button\" class=\"btn btn-default\" ng-click=\"move(-1)\"><i\n" +
    "                    class=\"material-icons\">keyboard_arrow_left</i></a>\n" +
    "        </div>\n" +
    "        <div class=\"calendar-header col s4 m8 l8\">{{title}}</div>\n" +
    "        <div class=\"col s4 m2 l2 right-align\">\n" +
    "            <a tooltipped  data-position=\"top\" data-delay=\"500\" data-tooltip=\"Siguiente semana\" type=\"button\" class=\"btn btn-default\" ng-click=\"move(1)\"><i\n" +
    "                    class=\"material-icons\">keyboard_arrow_right</i></a>\n" +
    "        </div>\n" +
    "    </div>\n" + 
    "    <weekview ng-switch-when=\"week\"></weekview>\n" +
    "</div>\n" +
    "");
}]);

angular.module("template/rcalendar/week.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/rcalendar/week.html",
        "<div>\n" +
    "    <table class=\"table table-fixed\">\n" +
    "        <thead>\n" +
    "        <tr>\n" +
    "            <th class=\"calendar-hour-column weekview-header-label\">Hora</th>\n" +
    "            <th ng-repeat=\"dt in dates\" class=\"text-center weekview-header-label\">{{dt.date| date:\n" +
    "                formatWeekViewDayHeader}}\n" +
    "            </th>\n" +
    "            <th ng-if=\"gutterWidth>0\" class=\"gutter-column\" ng-style=\"{width: gutterWidth+'px'}\"></th>\n" +
    "        </tr>\n" +
    "        </thead>\n" +
    "    </table>\n" +
    "    <div class=\"scrollable\" style=\"height: 400px\">\n" +
    "        <table class=\"table table-bordered table-fixed\">\n" +
    "            <tbody>\n" +
    "            <tr ng-repeat=\"row in rows track by $index\">\n" +
    "                <td class=\"calendar-hour-column text-center\">\n" +
    "                    {{row[0].time | date: formatHourColumn}}\n" +
    "                </td>\n" +
    "                <td ng-repeat=\"tm in row track by tm.time\" ng-class=\"{'cyan':selectedCell==tm.index, 'accent-4':true}\" ng-click=\"select(tm.time, tm.index)\">\n" +
    "                  <div ng-class=\"{'calendar-event-wrap': tm.events, 'not-active': true}\" ng-if=\"tm.events\">\n" +
    "                        <div ng-click=\"$event.stopPropagation();\" ng-repeat=\"displayEvent in tm.events\" class=\"calendar-event\"\n" +
    "                             ng-style=\"{left: -7+'%', width: 115/displayEvent.overlapNumber+'%', height: 3.55*((displayEvent.endIndex)-displayEvent.startIndex)+'em', top: -26+'px'}\">\n" +
    "                            <div class=\"calendar-event-inner grey lighten-1\">{{displayEvent.event.title}}</div>\n" +
    "                    </div>\n" +
    "                </td>\n" +
    "                <td ng-if=\"normalGutterWidth>0\" class=\"gutter-column\" ng-style=\"{width: normalGutterWidth+'px'}\"></td>\n" +
    "            </tr>\n" +
    "            </tbody>\n" +
    "        </table>\n" +
    "    </div>\n" +
    "</div>");
}]);
