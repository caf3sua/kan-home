(function() {
    'use strict';

	angular
	  	.module('kanHomeApp')
	  	.directive('rowSelect', rowSelect)
	  
    function rowSelect() {
    	  return {
    	    require: '^stTable',
    	    template: '<input type="checkbox">',
    	    scope: {
    	        row: '=rowSelect'
    	    },
    	    link: function (scope, element, attr, ctrl) {

    	      element.bind('click', function (evt) {

    	        scope.$apply(function () {

    	            ctrl.select(scope.row, 'multiple');

    	        });

    	      });

    	      scope.$watch('row.isSelected', function (newValue) {

    	        if (newValue === true) {

    	            element.parent().addClass('st-selected');
    	            element.find('input').attr('checked', true);

    	        } else {

    	            element.parent().removeClass('st-selected');
    	            element.find('input').attr('checked', false);

    	        }
    	      });
    	    }
    	  };
    	}
})();
