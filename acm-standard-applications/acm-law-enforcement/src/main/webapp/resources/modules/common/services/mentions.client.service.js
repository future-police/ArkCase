'use strict';

/**
 * @ngdoc service
 * @name services:Mentions.Service
 *
 * @description
 *
 * {@link https://gitlab.armedia.com/arkcase/ACM3/tree/develop/acm-standard-applications/acm-law-enforcement/src/main/webapp/resources/modules/common/services/mentions.client.service.js modules/common/services/mentions.client.service.js}
 *
 * Mentions.Service provides functions for mention functionality
 */
angular.module('services').factory('Mentions.Service', [ '$q', 'UtilService', 'LookupService', 'Ecm.EmailService', function($q, Util, LookupService, EcmEmailService) {
    return ({
        getUsers: getUsers,
        sendEmailToMentionedUsers: sendEmailToMentionedUsers
    });

    /**
     * @ngdoc method
     * @name getUsers
     * @methodOf services:Mentions.Service
     *
     * @description
     * Obtains a list of all users in ArkCase.
     *
     * @returns {Object} Promise
     */
    function getUsers() {
        // var usersList = [];
        var deferred = $q.defer();
        LookupService.getUsers().then(function (users) {
            _.forEach(users, function (user) {
                user.label = user.name;
            });
            deferred.resolve(users);
        });
        return deferred.promise;
    }

    /**
     * @ngdoc method
     * @name checkIfMentionedUsersStillExist
     * @methodOf services:Mentions.Service
     *
     * @description
     * Checks if the mentioned users were removed, if true - removes the email address from the list.
     *
     * @param {Array} emailAddresses  - array of all email addresses of the mentioned users in an input
     * @param {Array} usersMentioned  - array of all full names of the mentioned users in an input
     * @param {String} textMentioned  - the sentence where the user was mentioned
     *
     * @returns {Array} array with the email addresses
     */
    function checkIfMentionedUsersStillExist(emailAddresses, usersMentioned, textMentioned){
        _.forEach(usersMentioned, function (user, index) {
            if(!_.includes(textMentioned, user)){
                emailAddresses.splice(index, 1);
            }
        });
        return emailAddresses;
    }

    /**
     * @ngdoc method
     * @name sendEmailToMentionedUsers
     * @methodOf services:Mentions.Service
     *
     * @description
     * Sends email to the users who's email addressed where added to the array (emailAddresses).
     *
     * @param {Array} emailAddresses  - array of all email addresses of the mentioned users in an input
     * @param {Array} usersMentioned  - array of all full names of the mentioned users in an input
     * @param {String} objectType - type of the object where the user was mentioned
     * @param {Number} objectId  - id of the object where the user was mentioned
     * @param {String} textMentioned  - the sentence where the user was mentioned
     *
     * @returns {Object} Promise
     */
    function sendEmailToMentionedUsers(emailAddresses, usersMentioned, objectType, subType, objectId, textMentioned){
        if(!Util.isEmpty(emailAddresses)){
            var emailData = {};
            emailData.objectType = objectType;
            emailData.subType = subType;
            emailData.objectId = objectId;
            emailData.textMentioned = textMentioned;
            emailData.emailAddresses = checkIfMentionedUsersStillExist(emailAddresses, usersMentioned, textMentioned);
            EcmEmailService.sendMentionsEmail(emailData);
        }
    }
} ]);