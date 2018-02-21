/**
 * 
 *
 */
cbpdp.documentCluster = {
	init : function( options) {
		this.loadDocumentList( options );
		this.sourceSuggestionsToggle();
	},
	
	updateDocumentBrowser : function() {
		cbpdp.documentBrowser.initArticleWithDocumentBrowser('right');
	},
	
	sourceSuggestionsToggle : function() {
		$( '.switchButton' ).on( 'click', function() {
			$( '.clusterView' ).toggleClass('hide');
			$( '.suggestionView' ).toggleClass('hide');
			$( '.cluster-toggle' ).toggleClass('hide');
			$( '.cluster-toggle-sugg' ).toggleClass('hide');
			$( '.cluster-info' ).toggleClass('hide');
			$( '.suggestion-info' ).toggleClass('hide');
		});
	},
	
	loadDocumentList : function( options ) {
		var SourceId = cbpdp.documentCluster.getDocumentSourceId();
		$.ajax( {
			url: cbpdp.WWW_ROOT + 'reports/cluster/' + SourceId,
			type: 'POST',
		 	data: options
		}).done(function( data ){
			$( '#cluster' ).html(data);
			cbpdp.documentCluster.enableTabs();
			var ComparisonId = cbpdp.documentCluster.getDocumentComparisonId();
			if( ComparisonId && ComparisonId != SourceId ) {
				$( 'tr[data-document-id="' + ComparisonId + '"]' ).addClass('active');
			} else {
				$('tbody tr:first', '#cluster').addClass('active');
			}
		} );
	},
	
	getDocumentSourceId : function() {
		var documentSource = location.pathname;
		var pathname = documentSource.match(/\d+/);
		return pathname[0];
	},
	
	getDocumentComparisonId : function() {
		var documentSource = location.pathname;
		var pathname = documentSource.match(/\d+$/);
		if (pathname) {
			return pathname[0];
		} else {
			return false;
		}
	},
	
	enableTabs : function() {
		$( '#cluster-tabs a:first' ).tab('show');
	},
	
	loadEmphasisSaveForm : function() {
		var SourceId = cbpdp.documentCluster.getDocumentSourceId();
		$.ajax({
			url: cbpdp.WWW_ROOT + 'emphases/add/' + SourceId
			}).done(function( data ){
			$( '#EmphasisSaveForm' ).html(data);
			cbpdp.documentCluster.fillEmphasisSaveForm();
		});
		$( '#EmphasisIndexFormBox' ).hide();
		$( '#EmphasisCustomForm' ).hide();
		$( '#EmphasisSaveForm' ).show();
		$( '#btnEmphasisSave' ).hide();
	},
	
	loadEmphasisEditForm : function( element ) {
		console.log( $( element).attr('href') );

		var editUrl = $( element ).attr('href').substring(1, $( element).attr('href').length);
		$.ajax({
			url: $( element).attr('href')
			}).done(function( data ){
			$( '#EmphasisEditForm' ).html(data);
			});
		$( '#EmphasisIndexFormBox' ).hide();
		$( '#EmphasisCustomForm' ).hide();
		// $( '#EmphasisSaveForm' ).show();
		$( '#btnEmphasisSave' ).hide();
		$( '#EmphasisEditForm' ).show();
	},
	
	cancelEmphasisSaveForm : function() {
		$( '#EmphasisSaveForm' ).hide();
		$( '#EmphasisIndexFormBox' ).show();
		$( '#EmphasisCustomForm' ).show();
		$( '#btnEmphasisSave' ).show();
	},
	
	cancelEmphasisEditForm : function() {
		$( '#EmphasisEditForm' ).hide();
		$( '#EmphasisSaveForm' ).hide();
		$( '#EmphasisIndexFormBox' ).show();
		$( '#EmphasisCustomForm' ).show();
		$( '#btnEmphasisSave' ).show();
	},
	
	fillEmphasisSaveForm : function() {
		$( '#EmphasisCoefficientBc' ).attr('value', $('#ReportCoefficientBc').val());
		$( '#EmphasisCoefficientCc' ).attr('value', $('#ReportCoefficientCc').val());
		$( '#EmphasisCoefficientGct' ).attr('value', $('#ReportCoefficientGct').val());
		$( '#EmphasisCoefficientLccs' ).attr('value', $('#ReportCoefficientLccs').val());
		$( '#EmphasisCoefficientEncoplot' ).attr('value', $('#ReportCoefficientEncoplot').val());
		$( '#EmphasisCoefficientCpa' ).attr('value', $('#ReportCoefficientCpa').val());
		$( '#EmphasisCoefficientBcg' ).attr('value', $('#ReportCoefficientBcg').val());
		$( '#EmphasisCoefficientCocit' ).attr('value', $('#ReportCoefficientCocit').val());
		$( '#EmphasisCoefficientLucene' ).attr('value', $('#ReportCoefficientLucene').val());
	},
	
	showTooltipsOnAlgorithmLabels : function( element, e ) {
		if ( e.type == 'mouseover' ) {
			element.tooltip( {
				title : element.attr( 'title' ),
				placement : 'left',
				trigger : 'hover'
			} ).tooltip( 'show' );	
		} else if ( e.type == 'mouseout' ) {
			element.tooltip( 'hide' );
		};
	},
	
	changeLinkParams : function( element, e ) {
		var editUrl = $('#btnEmphasisEdit').attr('href');
		var editArr = editUrl.split('/');
		editArr[editArr.length -1] = $(element).val();
		$('#btnEmphasisEdit').attr('href', editArr.join('/'));
		
		var delUrl = $('#btnEmphasisDelete').prev().attr('action');
		var delArr = delUrl.split('/');
		delArr[delArr.length -1] = $( element ).val();
		$('#btnEmphasisDelete').prev().attr('action', delArr.join('/'));
	},
	
	checkForm : function ( element, e ) {
		console.log($(element).val().length);
		if($(element).val().length > 0) {
			$(element).parent().parent().removeClass('error');
			$( '#btnEmphasisAddSave' ).attr('disabled', false);
			$( '#btnEmphasisEditSave' ).attr('disabled', false);
		} else {
			$(element).parent().parent().addClass('error');
			$( '#btnEmphasisAddSave' ).attr('disabled', true);
			$( '#btnEmphasisEditSave' ).attr('disabled', true);	
		}
		
		
	}
};
