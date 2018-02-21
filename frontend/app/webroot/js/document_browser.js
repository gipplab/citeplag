/**
 * 
 *
 */
cbpdp.documentBrowser = {
	draggable : false,
	/**
	 * initializises the DocumentBrowser 
	 */
	init : function() {
		cbpdp.dom.documentBrowser = $( '.document-browser' );
		
		this.initArticleWithDocumentBrowser( 'left' );
		this.initArticleWithDocumentBrowser( 'right' );
		
		// init to show cit marks for one procedure and connection lines
		this.changeAlgorithmSelection( $( '.algorithm-selection .active', cbpdp.dom.navigation ) );
		$('.fncitations').hide();
		this.bindEvents();
	},
	
	initArticleWithDocumentBrowser : function( name ) {
		var $article = $( 'article.' + name, cbpdp.dom.documents );
		
		/**
		 * there are three different heights for the citation marks in document browser to provide usability for 'short', 'medium' and 'long' documents
		 * this is necessary because e.g. on long documents with high citation plag. density the marks could overlap otherwise
		 */
		var $articleContent = $( '.content', $article );
		var articleContentHeight = $articleContent.innerHeight();
		if ( articleContentHeight > 25000 ) {
			if ( articleContentHeight > 50000 ) {
				cbpdp.dom.documentBrowser.addClass( 'factor1' );
			} else {
				cbpdp.dom.documentBrowser.addClass( 'factor3' );
			}
		};
		this.drawCitationMarks( name );
		// this.bindFisheye( name );
	},
	
	renderSections : function( quantity, $documentBrowserForArticle ) {
		var pagesSetupArray = [];
		for ( var i = 0; i < quantity; i++ ) {
			$( $documentBrowserForArticle ).append( '<section></section>' );
			pagesSetupArray.push( { textpattern_length : 0 } );
		};
		return pagesSetupArray;
	},
	
	drawCitationMarks : function( name ) {
		var $article = $( 'article.' + name, cbpdp.dom.documents );
		var $documentBrowserForArticle = $( '.' + name, cbpdp.dom.documentBrowser );
		$( 'section', $documentBrowserForArticle ).remove();

		var $articleContent = $( '.content', $article );
		var articleContentHeight = $articleContent.innerHeight();
		var articleContentWidth = $articleContent.innerWidth();

		cbpdp.documentBrowser.updateOverlayHeight( 'left' );
		cbpdp.documentBrowser.updateOverlayHeight( 'right' );

		/** adds a section elements for every pseude page in document */
		var pagesSetupArray = this.renderSections( articleContentHeight / ( cbpdp.LINE_HEIGHT * cbpdp.LINES_PER_PAGE ), $documentBrowserForArticle );
		this.addTextPlagiarismDensity( pagesSetupArray, $article, $documentBrowserForArticle );

		var page = $( 'section:first', $documentBrowserForArticle );

		/** renders a citation mark for each citation that is found in document */
		$.each( $( 'cite', $article ).not('fn cite'), function( index, value ) {
			var $item = $( value );
			var position = $item.position();
			var pageIndex = parseInt ( position.top / ( cbpdp.LINES_PER_PAGE * cbpdp.LINE_HEIGHT ) );
			// copy citation from document text to document-browser
			var newCitation = $item.clone( false );
			$( 'section:eq(' + pageIndex + ')', $documentBrowserForArticle ).append( newCitation );
			$citation = newCitation;

			// compute and set the position of the citation mark
			var top = ( position.top / articleContentHeight ) * cbpdp.documentsHeight - ( pageIndex * page.outerHeight() ) - ( $citation.innerHeight() / 2 );
			var left = ( position.left / articleContentWidth ) * $documentBrowserForArticle.innerWidth();
			$citation.css( 'top', parseInt( top / page.outerHeight() * 100 ) + '%' );
			$citation.css( 'left', parseInt( left / page.outerWidth() * 100 ) + '%' );
		});
	},
	
	scrollDocumentWithDraggedOverlay : function( name, pos ) {
		var $article = $( 'article.' + name, cbpdp.dom.documents );
		var $articleContent = $( '.content', $article );		
		var articleContentHeight = $articleContent.innerHeight();

		/** attaches the with-scrolling behaviour to the viewport indication in document browser */
		var ratio = ( articleContentHeight / cbpdp.documentsHeight );
		var pointToMoveOverlayTo = ( pos * ratio );

		$article.scrollTop( pointToMoveOverlayTo );
	},
	
	/** Fisheye **/
	/** actually the sections are grabbed by a distance of 100px to the middle-point of the overlay. a working algorithm for computing a scale-value need to be implemented **/
	bindFisheye : function( name ) {
		var sections = $( '.' + name + ' section', cbpdp.dom.documentBrowser );
		var sectionHeight = sections.first().innerHeight();
		var sectionHeightHalf = sectionHeight / 2;

		var $overlay = $( '.' + name + ' .overlay-viewport', cbpdp.dom.documentBrowser );

		$( 'article.' + name, cbpdp.dom.documents ).scroll( function() {
			var overlayMiddle = parseInt( $overlay.position().top + $overlay.innerHeight() / 2 );

			sections.each( function( index, section ) {
				var $section = $( section );
				var sectionMiddle = parseInt( ( index + 1 ) * sectionHeight - sectionHeightHalf );				

				// just to specify a range. should be made dynamically
				if ( overlayMiddle >= ( sectionMiddle - 100 ) && overlayMiddle <= ( sectionMiddle + 100 ) ) {
					distance = ( sectionMiddle - overlayMiddle );

					var scale = 10 + ( 10 / Math.abs( distance ) );
					scale = 2 - ( 1 / scale );
					if ( scale != Infinity ) {
						scale = ( scale >= 5 ) ? 5 : parseInt( scale * 1000 ) / 1000;
						$section.css( 'box-flex', scale.toString() );
					};
				} else {
					$section.css( 'box-flex', ''+ 1 + '' );
				};
			} );
		} );
	},
	
	startDragging : function( element, e  ) {
		cbpdp.documentBrowser.draggable = true;
		var name = element.closest( 'aside' ).attr( 'class' );
		var offsetY = ( e.offsetY );
		$( document ).on( 'mousemove', function( e ) {
			cbpdp.documentBrowser.overlayMoveListener( name, element, e );
		} );
	},
	
	stopDragging : function( e ) {
		cbpdp.documentBrowser.draggable = false;
		$( document ).off( 'mousemove' );
		// this.scrollDocumentWithDraggedOverlay( name, e.clientY );
	},
	
	overlayMoveListener : function( name, element, e ) {
		// TODO fixes only the top-limitation for dragging at the moment. limitation for the button is still needed
		if ( e.clientY >= cbpdp.BASE_OFFSET && e.clientY <= ( cbpdp.documentsHeight ) ) {
			element.offset( {
				top : e.clientY
			} );
			this.scrollDocumentWithDraggedOverlay( name, e.clientY );
		}
	},
	
	articleScrollListener : function( element, e ) {
		/** attaches the with-scrolling behaviour to the viewport indication in document browser */
		var $articleContent = $( '.content', element );
		var ratio = ( $articleContent.innerHeight() / cbpdp.documentsHeight );
		var $overlay = $( '.' + e.currentTarget.className + ' .overlay-viewport', cbpdp.dom.documentBrowser );

		if ( cbpdp.documentBrowser.draggable == false ) {
			var pointToMoveOverlayTo = - parseInt( $articleContent.position().top / ratio );
			$overlay.css( 'top', pointToMoveOverlayTo );
		};
	},
	
	showPatternDetails : function( identifiers, procedure ) {
		var matchingIdentifier = cbpdp.documentBrowser.getMatchingIdentifier( identifiers, procedure );
		
		var pattern = matchingIdentifier.pattern_id;
		var count = matchingIdentifier.count;
		$.getJSON( cbpdp.WWW_ROOT + 'api/getPatternById/' + pattern, function( data ) {
			$.each( data.CitationpatternMember, function( index, value ) {
			} );
			cbpdp.dom.modal.html( new EJS( {
				url : cbpdp.WWW_ROOT + 'js/templates/document_browser_citation_modal.ejs'
			} ).render( {
				pattern : data
			} ) );
			cbpdp.dom.modal.modal();
		} );
	},
	
	getMatchingIdentifier : function ( identifiers, procedure ) {
		var matchingIdentifier = false;
		if ( identifiers === undefined ) {
			return matchingIdentifier;
		};
		var copyOfidentifiers = identifiers.slice( 0 );
		do {
			var next = copyOfidentifiers.pop();
			if ( next ) {
				if ( next.procedure == procedure ) {
					matchingIdentifier = next;
				};
			} else { break };
		} while	( !matchingIdentifier );
		return matchingIdentifier;
	},

	/**
	 * @DEPRECATED
	 */
	toggleAlgorithmSelection : function( element ) {
		element.toggleClass( 'icon-chevron-down' ).toggleClass( 'icon-chevron-up' );
		element.parent().toggleClass( 'collapsed' );
	},
	
	changeAlgorithmSelection : function( element ) {
		var procedures = cbpdp.helpers.stringExplode( element.data( 'procedure' ), '_' );
		// $( 'cite', cbpdp.dom.documentBrowser ).hide();
		$('cite', cbpdp.dom.documents).removeClass( 'highlight h0 h1 h2 h3 h4 h5 h6 h7 h8 h9 h10 h11 h12 h13' );
		$.each( procedures, function( key, procedure ) {
			$( 'cite.procedure_' + procedure, cbpdp.dom.documentBrowser ).show().each( function( index, value ) {
				cbpdp.documentBrowser.colorizeCite( $( value ), procedures );
			} );
		} );
		this.drawConnectionBezierCurves( procedures );
	},
	
	drawConnectionBezierCurves : function( procedures ) {
		cbpdp.dom.canvas[0].width = cbpdp.dom.documentBrowser.innerWidth() * 0.9;
		cbpdp.dom.canvas[0].height = cbpdp.dom.documentBrowser.innerHeight();
		
		var canvasOffset = cbpdp.dom.canvas.offset();
		var context = cbpdp.dom.canvas[0].getContext( '2d' );
		context.clearRect( 0, 0, canvas.width, canvas.height );
		context.lineWidth = 1;

		var sampleCite = $( '.left cite.match', cbpdp.dom.documentBrowser ).eq( 0 );
		halfCiteWidth = sampleCite.width() / 2;
		halfCiteHeight = sampleCite.height() / 2;

		// for each cite on the left cite render the conection lines
		$.each( $( '.left cite.match', cbpdp.dom.documentBrowser ), function( index, value ) {
			var startElement = $( value );
			if ( startElement.is( ':visible' ) ) {
				var startPosition = startElement.position();
				var startOffset = startElement.offset();
				var startx = startPosition.left + halfCiteWidth * 2;
				var starty = startOffset.top - cbpdp.BASE_OFFSET + halfCiteHeight;
				var cp1x = startPosition.left + 50;
				var cp1y = startOffset.top - cbpdp.BASE_OFFSET + 20;
				// since a cite can belong to multiple procedures, it's possible to render multiple connection lines for one single cite
				var identifiers = startElement.data( 'identifier' );
				for ( i in identifiers ) {
					if ( $.inArray( identifiers[i].procedure, procedures ) != -1 ) {
						
						// selector needs to be modfied if there is 1:1 connection between citations or 1:n
						if ( identifiers[i].procedure == 1 || identifiers[i].procedure == 21 ) {
							var endSelector = 'cite[data-identifier*="pattern_id":"' + identifiers[i].pattern_id + '","count":"' + identifiers[i].count + '"][data-document_id="' + startElement.data( 'document_id' ) + '"]';
						} else {
							var endSelector = 'cite[data-identifier*="pattern_id":"' + identifiers[i].pattern_id + '"][data-document_id="' + startElement.data( 'document_id' ) + '"]';
						};
						
						var endElements = $( '.right ' + endSelector, cbpdp.dom.documentBrowser );
						if ( endElements.length > 0 ) {
							// connect startelement with all matching endings
							$.each( endElements, function( index, value ) {
								var endElement = $( value );
								var endPosition = endElement.position();
								var endOffset = endElement.offset();

								var cp2x = endOffset.left - canvasOffset.left - 50;
								var cp2y = endOffset.top - cbpdp.BASE_OFFSET + halfCiteHeight - 20;
								var endx = endOffset.left - canvasOffset.left;
								var endy =  endOffset.top - cbpdp.BASE_OFFSET + halfCiteHeight;

								context.strokeStyle = '#999999';
								context.beginPath();
								context.moveTo( startx, starty - 1 );
								context.bezierCurveTo( cp1x, cp1y - 1, cp2x, cp2y - 1, endx, endy - 1 );
								context.stroke();

								context.beginPath();
								context.moveTo( startx, starty );
								context.bezierCurveTo( cp1x, cp1y, cp2x, cp2y, endx, endy );
								context.strokeStyle = startElement.css( 'background-color' );
								context.stroke();
								
							} );
						};
					};
				};
			};
		} );
	},
	
	colorizeCite : function( element, procedures ) {
		var identifiers = element.data( 'identifier' );
		for ( i in identifiers ) {
			if ( $.inArray( identifiers[i].procedure, procedures ) != -1 ) {
				var side = element.closest( 'aside' ).attr( 'class' );
				if (procedures == '1') {
					element.addClass( 'highlight' ).addClass( 'h' + element.data('document_id') % 14 );
					$( 'article.' + side + ' cite[data-citation_id="' + element.data('citation_id') + '"]', cbpdp.dom.documents).addClass( 'highlight' ).addClass( 'h' +  element.data('document_id') % 12 );
				} else {
					element.addClass( 'highlight' ).addClass( 'h' + identifiers[i].pattern_id % 14 );
					$( 'article.' + side + ' cite[data-identifier*="pattern_id":"' + identifiers[i].pattern_id + '"]', cbpdp.dom.documents ).addClass( 'highlight' ).addClass( 'h' + identifiers[i].pattern_id % 14 );
				}
			}
		}
	},
	
	addTextPlagiarismDensity : function( pages, $article, $documentBrowserForArticle ) {
		$.each( $( 'mark', $article ), function( index, value ) {
			var $item = $( value );
			var position = $item.position();
			tmpitem = $item;
			var cnt = 0;
			while (position.top <= 0 && tmpitem.parent().length > 0) {
				tmpitem = $(tmpitem).parent();
				position = tmpitem.position();
				cnt++;
			}
			var pageIndex = parseInt ( position.top / ( cbpdp.LINES_PER_PAGE * cbpdp.LINE_HEIGHT ) );
			pages[ pageIndex ].textpattern_length += $item.data( 'length' );
		});
		
		$.each( pages, function( index, value ) {
			$( 'section', $documentBrowserForArticle ).eq( index ).css( 'background', 'rgba(255, 0, 0, ' + pages[ index ].textpattern_length / 3600 + ')' );
		} );
	},
	
	/** gets the viewport indicator in document-browser and resizes it in aspect to document length */
	updateOverlayHeight : function( name ) {
		var $article = $( 'article.' + name, cbpdp.dom.documents );
		var $documentBrowserForArticle = $( '.' + name, cbpdp.dom.documentBrowser );

		var $articleContent = $( '.content', $article );
		var articleContentHeight = $articleContent.innerHeight();

		/** gets the viewport indicator in document-browser and resizes it in aspect to document length */
		var $overlay = $( '.' + name + ' .overlay-viewport', cbpdp.dom.documentBrowser );
		var overlayHeight = ( cbpdp.documentsHeight / articleContentHeight ) * cbpdp.documentsHeight;
		$overlay.innerHeight( overlayHeight / cbpdp.documentsHeight * 100 + '%' );
	},
	
	handleCite : function( eventType, citationId ) {
		if ( eventType == 'mouseleave' ) {
			$( 'cite.hover', cbpdp.dom.documents ).removeClass( 'hover' );
		} else {
			var selector = 'cite[data-citation_id="' + citationId + '"]';
			if ( eventType == 'mouseenter' ) {
				$( selector, cbpdp.dom.documents ).addClass( 'hover' );
			};
			if ( eventType == 'mousedown' ) {
				var $matches = $( selector, cbpdp.dom.documentBrowser );
				$matches.toggleClass( 'selected' );
		
				var elementLeft =	$(selector, '#doc_a').eq(0);
				if(!elementLeft.is(':visible') && elementLeft.parent('fn').length > 0) {
					elementLeft.parent('fn').show();
					$('#fncitations' + elementLeft.parent('fn').attr('id')).hide();
				}
				if ( elementLeft.length > 0 ) {
					$( '#doc_a' ).scrollTo( elementLeft, 500 );
				};
		
				var elementRight = $(selector, '#doc_b').eq(0);
				if(!elementRight.is(':visible') && elementRight.parent('fn').length > 0) {
					elementRight.parent('fn').show();
					$('#fncitations' + elementRight.parent('fn').attr('id')).hide();
				}
				if ( elementRight.length > 0 ) {
					$( '#doc_b' ).scrollTo( elementRight, 500 );
				};
			};
		}
	},
	
	handleCiteWhichIsCitationpatternmember : function( eventType, element, procedure ) {
		var identifiers = element.data( 'identifier' );
		var documentId = element.data( 'document_id' );

		if ( eventType == 'mouseleave' ) {
			$( 'cite.hover', cbpdp.dom.documents ).removeClass( 'hover' );
		} else {
			var matchingIdentifier = cbpdp.documentBrowser.getMatchingIdentifier( identifiers, procedure );
			var selector = 'cite[data-identifier*="pattern_id":"' + matchingIdentifier.pattern_id + '"][data-document_id="' + documentId + '"]';
			if ( eventType == 'mouseenter' ) {
				$( selector, cbpdp.dom.documents ).addClass( 'hover' );
			};
			if ( eventType == 'mousedown' ) {
				var $matches = $( selector, cbpdp.dom.documentBrowser );
				$matches.toggleClass( 'selected' );

				var elementLeft;
				if ( element.closest( 'article.left' ).length == 0 ) {
					elementLeft = $( selector, '#doc_a' ).eq(0);
				} else {
					elementLeft = element;
				};
				if(elementLeft.parent('.fncitations').length > 0) {
					elementLeft = $('cite[data-citation_id="' + elementLeft.data('citation_id') + '"]', elementLeft.parent('.fncitations').prev('fn'));
				}
				if(!elementLeft.is(':visible') && elementLeft.parent('fn').length > 0) {
					elementLeft.parent('fn').show();
					$('#fncitations' + elementLeft.parent('fn').attr('id')).hide();
				}
				if ( elementLeft.length > 0 ) {
					$( '#doc_a' ).scrollTo( elementLeft, 500 );
				};

				var elementRight;
				if ( element.closest('article.right').length == 0 ) {
					elementRight = $(selector, '#doc_b').eq(0);
				} else {
					elementRight = element;
				}
				if(elementRight.parent('.fncitations').length > 0) {
					elementRight = $('cite[data-citation_id="' + elementRight.data('citation_id') + '"]', elementRight.parent('.fncitations').prev('fn'));
				}
				if(!elementRight.is(':visible') && elementRight.parent('fn').length > 0) {
					elementRight.parent('fn').show();
					$('#fncitations' + elementRight.parent('fn').attr('id')).hide();
				}
				if ( elementRight.length > 0 ) {
					$( '#doc_b' ).scrollTo( elementRight, 500 );
				};
			};
		}
	},
	
	bindEvents : function() {
		$(' .overlay-viewport ', cbpdp.dom.documentBrowser ).on( 'mousedown', function( e ) {
			cbpdp.documentBrowser.startDragging( $( this ), e );
		} );

		$( cbpdp.dom.documentBrowser ).on( 'mousedown mouseenter mouseleave', 'cite.match', function( e ) {
			cbpdp.documentBrowser.handleCiteWhichIsCitationpatternmember( e.type, $( this ), $( '.algorithm-selection .active', cbpdp.dom.navigation ).data( 'procedure' ) );
		} );
		
		$( cbpdp.dom.documentBrowser ).on( 'mousedown mouseenter mouseleave', 'cite:not(.match)', function( e ) {
			cbpdp.documentBrowser.handleCite( e.type, $( this ).data( 'citation_id' ) );
		} );
		
		$( window ).resize( function() {
			cbpdp.documentsHeight = cbpdp.dom.documents.innerHeight();
			cbpdp.documentsWidth = cbpdp.dom.documents.innerWidth();
			
			cbpdp.documentBrowser.updateOverlayHeight( 'left' );
			cbpdp.documentBrowser.updateOverlayHeight( 'right' );
		} );
	}

};
