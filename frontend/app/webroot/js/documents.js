/**
 *
 *
 */
cbpdp.documents = {

	init : function() {
		cbpdp.dom.documents = $('.document');
		$('#doc_a fn').each(function(i, e) { e.id = 'a' + e.id; });
		$('#doc_b fn').each(function(i, e) { e.id = 'b' + e.id; });

		//$('fn').hide();
		$('fn').each(function(i, e) {
			ref = $('<a href="#" onclick="$(\'article fn#' + e.id + '\').toggle(); $(\'#fncitations' + e.id + '\').toggle();" class="footnote">' + e.id.replace(/[ab]0*(\d+)/, 'Footnote&nbsp;$1') + '</a>');
			$(ref)
				.powerTip({
					placement: 'ne',
					smartPlacement: true,
					mouseOnToPopup: true,
					closeDelay: 300,
					intentPollInterval: 300
				})
				.data('powertip', $(e).clone().removeAttr('id').show());
			$(e).before(ref);
			$(e).before(" ");
			$(e).after('<span id="fncitations' + e.id + '" class="fncitations"></span>');
		});

		$('fn cite').each(function(i, e) { $('#fncitations' + $(e).parent('fn').attr('id')).append($(e).clone()); });
		$('mark').each(function(index, el) {
			$(el).powerTip({
				placement: 'n',
				smartPlacement: true,
				mouseOnToPopup: true,
				closeDelay: 300,
				intentPollInterval: 300
			})
			.data('powertip', 'Length: ' + $(el).data( 'length' ) + '<br />Complete Text: ' + $(el).data('complete'));
		});

		$('cite').each(function(index, el) {
			$(el).powerTip({
				placement: 'n',
				smartPlacement: true,
				mouseOnToPopup: true,
				closeDelay: 300,
				intentPollInterval: 300
			});
			$(el).data('powertip', function() { 
				d = null;
				a = null;
				$.ajax({ async: false, url: cbpdp.WWW_ROOT + 'api/getDocument/' + $(el).data('document_id'), dataType: "json", success: function(data) { d = data; }});
				$.ajax({ async: false, url: cbpdp.WWW_ROOT + 'api/getAuthors/' + $(el).data('document_id'), dataType: "json", success: function(data) { a = data; }});
				return new EJS({url : cbpdp.WWW_ROOT + 'js/templates/document_citation_popover.ejs'}).render({ authors : a, documentData : d});
			});
		});

		cbpdp.documentsHeight = cbpdp.dom.documents.innerHeight();
		cbpdp.documentsWidth = cbpdp.dom.documents.innerWidth();

		this.bindEvents();
	},
	
	/** scrolls both documents to the selected text-matches and shows the mask */
	scrollToMatch : function( element ) {
		var $matches
		var selector;
		if ( element.is( 'cite' ) ) {
 			selector = 'cite[data-identifier=' + element.data( 'identifier' ) + ']';
		};
		
		if ( element.is( 'mark' ) ) {
			selector = 'mark[data-pattern_id=' + element.data( 'pattern_id' ) + ']';
		};

		$matches = $( selector, cbpdp.dom.documents );
		$matches.toggleClass( 'selected' );

		if ( element.hasClass( 'selected' ) ) {			
			cbpdp.dom.mask.addClass( 'gradient' ).fadeTo( 500, 1 );
			cbpdp.dom.documents.addClass( 'mask-text' );
			cbpdp.helpers.showHint( "Press 'Esc'-key or double click to hide the mask." );
			try {
				elementLeft = $( selector, '#doc_a' ).eq(0);
				if(!elementLeft.is(':visible') && elementLeft.parent('fn').length > 0) {
					elementLeft.parent('fn').show();
					$('#fncitations' + elementLeft.parent('fn').attr('id')).hide();
				}
				$( '#doc_a' ).scrollTo(elementLeft, 500, {
					'offset': {
						top: -200
					}
				} );
			} catch ( e ) {
				console.error( "Couldn't scroll to position on document A" );
			}

			try {
				elementRight = $( selector, '#doc_b' ).eq(0);
				if(!elementRight.is(':visible') && elementRight.parent('fn').length > 0) {
					elementRight.parent('fn').show();
					$('#fncitations' + elementRight.parent('fn').attr('id')).hide();
				}
				$( '#doc_b' ).scrollTo(elementRight, 500, {
					'offset': {
						top: -200
					}
				} );
			} catch ( e ) {
				console.error( "Couldn't scroll to position on document B" );
			}
		}
	},
	


	/** removes focus and mask on text-matches */
	removeSelectionAndMask : function() {
		$( '.selected', cbpdp.dom.documents ).removeClass( 'selected' );
		cbpdp.dom.mask.removeClass( 'gradient' );
		cbpdp.dom.documents.removeClass( 'mask-text' );
		cbpdp.helpers.hideHint();
	},
	
	/** adds and removes Tooltips and hover effects to Text text-matches*/
	showAndHideTextPlagiarismTooltips : function( element, eventType ) {
		var $matches = $( 'mark[data-pattern_id=' + element.data( 'pattern_id' ) + ']', cbpdp.dom.documents )
		if ( eventType == 'mouseover' ) {			
			$matches.addClass( 'hover' );
		} else if ( eventType == 'mouseout' ) {
			$matches.removeClass( 'hover' );
		}
	},
	
	bindEvents : function() {
		$( 'article' ).on( 'scroll click', function( e ) {
			cbpdp.documentBrowser.articleScrollListener( $( this ), e );
		} );
		$('article cite:not(.match)', cbpdp.dom.documents).on('mousedown mouseenter mouseleave', function( e ) {
			cbpdp.documentBrowser.handleCite( e.type, $( this ).data( 'citation_id' ) );
		} );
		$( 'article cite.match', cbpdp.dom.documents ).on( 'mousedown mouseenter mouseleave', function( e ) {
			cbpdp.documentBrowser.handleCiteWhichIsCitationpatternmember( e.type, $( this ), $( '.algorithm-selection .active', cbpdp.dom.navigation ).data( 'procedure' ) );
		} );
		
		$( 'article mark', cbpdp.dom.documents ).on( 'click', function() {
			cbpdp.documents.scrollToMatch($(this));
		} );

		/** shows tooltip and hover effect for text plagiarism mouseover in documents */
		$( 'article mark', cbpdp.dom.documents ).on( 'mouseover', function( e ) {
			cbpdp.documents.showAndHideTextPlagiarismTooltips( $( this ) , e.type );
		} );
		
		/** hides tooltip and hover effect for text plagiarism mouseover in documents */
		$( 'article mark', cbpdp.dom.documents ).on( 'mouseout', function( e ) {
			cbpdp.documents.showAndHideTextPlagiarismTooltips( $( this ) , e.type );
		} );
	}

};
