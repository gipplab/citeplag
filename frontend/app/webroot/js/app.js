/**
 *
 *
 */
var cbpdp = {
	WWW_ROOT : '/',
	BASE_OFFSET : 51, // in px
	LINES_PER_PAGE : 86,
	LINE_HEIGHT : 20,

	viewportHeight : window.innerHeight,

	/** dom elements which are always available, never change, and get reused over all classes and functions */
	dom : {
		canvas : undefined,
		documents : undefined,
		documentCluster : undefined,
		documentBrowser : undefined,
		modal : undefined,
		mask : undefined,
		navigation : undefined,
	},
	
	documentsHeight : undefined,
	documentsWidth : undefined,

	init : function() {
		this.dom.canvas = $( '#canvas' );
		this.dom.modal = $( '#modal' );
		this.dom.mask = $( '.mask' );

		this.checkBrowser();
	},
	
	/**
	 * checks the browser and its version
	 */
	checkBrowser : function() {
        if ($.cookie('browserWarn')) {
            return;
        }
		if ( /Firefox[\/\s](\d+\.\d+)/.test( navigator.userAgent ) ) {
 			var firefoxVersion = new Number(RegExp.$1);
            if (firefoxVersion < 22) {
    			alert( "This application does not support your version of Firefox completely. Please update your browser for best experience!" );
                $.cookie('browserWarn', true, {'path': '/'});
			}
		}
		if ( /Opera[\/\s](\d+\.\d+)/.test( navigator.userAgent ) ) {
 			var operaVersion = new Number(RegExp.$1);
 			if ( operaVersion < 11 ) {
				alert( "This application does not support Opera 10 and before. Please update your browser!" );
                $.cookie('browserWarn', true, {'path': '/'});
 			}
		}
		if ( /MSIE (\d+\.\d+);/.test( navigator.userAgent ) ) {
			var ieVersion = new Number(RegExp.$1);
			if ( ieVersion < 10 ) {
				this.hideNavigationElements();
				alert( "This application does not support Internet Explorer 9 and before. Please use the current version of Google Chrome or Mozilla Firefox for best experience!" );
                $.cookie('browserWarn', true, {'path': '/'});
				$( '#wrapper' ).load( 'templates/ie.html' );
			} else {
				alert( "This application does not support Internet Explorer completely. Please use the current version of Google Chrome or Mozilla Firefox for best experience!" );
                $.cookie('browserWarn', true, {'path': '/'});
			}
		}
	}

};
