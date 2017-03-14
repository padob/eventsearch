(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .config(bootstrapMaterialDesignConfig);

    compileServiceConfig.$inject = [];

    function bootstrapMaterialDesignConfig() {
        $.material.init();

    }
})();
