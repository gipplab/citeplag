/**
 * Binding for all events over the DOM, e.g. mouse-, key-, (DOM)change-events.
 * Aggregates by functions for each other class which represents a GUI sub section, e.g. DocumentBrowser, DocumentCluster, Navigation.
 */
cbpdp.events = {
	
	windowResized : false,
	
	init : function() {
		this.bindGeneralEvents();
	},
	
	bindGeneralEvents : function() {
		/** the draggable function for the overlay-viewport*/
		$( document ).mouseup( function( e ) { 
			if ( cbpdp.documentBrowser.draggable === true ) {
				cbpdp.documentBrowser.stopDragging();
				// $(' .overlay-viewport ', cbpdp.dom.documentBrowser ).removeClass('overlay-viewport-moving');
			};
		} );
		
		$( document ).dblclick( function() {
			cbpdp.documents.removeSelectionAndMask();
		} );
		
		/** Close all navigation elements with Esc Button */
		$( document ).keyup( function( e ) {
			if ( e.keyCode == 27 ) {
				cbpdp.navigation.hideCollapsedPanel();
				cbpdp.navigation.removeAllActiveFromNavigation();
				cbpdp.documents.removeSelectionAndMask();
			}
		} );
		
		$( window ).resize( function( e ) {
			if ( !cbpdp.events.windowResized ) {
				if ( cbpdp.dom.documents.hasClass( 'settings-documentbrowser-invisible' ) ) {
					cbpdp.dom.documents.removeClass( 'settings-documentbrowser-invisible' );
					cbpdp.dom.mask.addClass( 'no-browser' );
					cbpdp.helpers.showHint( "Document Browser removed due to resize of the window. Please reload page." );	
				};
			};
			cbpdp.events.windowResized = true;
		} );
		
		/** Render modal with template content */
		$( document ).on( 'click', '[data-modal="modal"]', function( e ) {
			e.preventDefault();
			cbpdp.dom.modal.html( new EJS( { url: $( this ).attr( 'href' ) } ).render() );
			cbpdp.dom.modal.modal();
			return false;
		} );
		
		$( document ).on( 'click', '.popover .close', function( event ) {
			$( this ).closest( '.popover' ).remove();
		} );
	},

};