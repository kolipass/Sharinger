/*! socialSharing v0.1 | 2014 */
(function($){
    var socialSharing = {
        /**
         * Список сервисов
         */
        listService: {
            vkontakte: function(setting) {
                url  = 'http://vkontakte.ru/share.php?';
                url += 'url='          + encodeURIComponent(setting.url);
                url += '&title='       + encodeURIComponent(setting.title);
                url += '&description=' + encodeURIComponent(setting.description);
                url += '&image='       + encodeURIComponent(setting.img);
                url += '&noparse=true';
                return url;
            },
            odnoklassniki: function(setting) {
                url  = 'http://www.odnoklassniki.ru/dk?st.cmd=addShare&st.s=1';
                url += '&st.comments=' + encodeURIComponent(setting.message);
                url += '&st._surl='    + encodeURIComponent(setting.url);
                return url;
            },
            facebook: function(setting) {
                url  = 'http://www.facebook.com/sharer.php?s=100';
                url += '&p[title]='     + encodeURIComponent(setting.title);
                url += '&p[summary]='   + encodeURIComponent(setting.description);
                url += '&p[url]='       + encodeURIComponent(setting.url);
                url += '&p[images][0]=' + encodeURIComponent(setting.img);
                return url;
            },
            twitter: function(setting) {
                url  = 'http://twitter.com/share?';
                url += 'text='      + encodeURIComponent(setting.message);
                //url += '&url='      + encodeURIComponent(setting.url);
                url += '&counturl=' + encodeURIComponent(setting.url);
                return url;
            },
            google: function(setting) {
                url  = 'https://plus.google.com/share?';
                url += '&url=' + encodeURIComponent(setting.url);
                return url;
            }
        },

        popup: function(url) {
            window.chrome.tabs.create({
                url: url,
                active: false
            }, function(tab) {
                window.chrome.windows.create({
                    tabId: tab.id,
                    type: 'popup',
                    focused: true
                });
            });
        },

        init: function(options) {
            return this.each(function() {
                $(this).off('click.socialSharing').on('click.socialSharing', function() {
                    var setting = $.extend({
                        service: null,
                        url: null,
                        title: '',
                        description: '',
                        message: '', //title+description
                        img: null
                    }, $(this).data(), options);

                    if (socialSharing.listService[setting.service]) {
                        var url = socialSharing.listService[setting.service].call(null, setting);
                        socialSharing.popup(url);
                    }
                });
            });
        }
    };

    $.fn.socialSharing = function(method){
        if ( socialSharing[method] ) {
            return socialSharing[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof method === 'object' || ! method ) {
            return socialSharing.init.apply( this, arguments );
        }
    };

    /**
     * @param url
     * @param title
     * @param description
     * @param message title + description
     */
    window.initSocialSharing = function(url, title, description, message) {
        $.each(jQuery('#social-sharing').html('').data('services').split(','), function(k, val) {
            jQuery('#social-sharing').append('<span class="social-sharing" data-service="'+ val +'"></span>');
            jQuery('.social-sharing').socialSharing({
                url: url,
                title: title,
                description: description,
                message: message
            });
        });

        jQuery('#social-sharing').show(300);
    }
})(jQuery);
