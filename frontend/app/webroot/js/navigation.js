/**
 *
 *
 */
cbpdp.navigation = {
	
	navStatistics : undefined,
	navSettings : undefined,
	navDocuments : undefined,
	navCluster : undefined,
	
	/** dom elements which are always available, never change, and get reused over all classes and functions */
	highlightText : undefined,
	highlightCitation : undefined,
	highlightMatchingcitation : undefined,
	highlightConnection : undefined,
	documentbrowserShow : undefined,
	panelCloseBtn : undefined,

	init : function() {
		this.navStatistics = $( '.statistics-panel' );
		this.navSettings = $( '.settings-panel' );
		this.navDocuments = $( '.documents-panel' );
		this.navCluster = $( '.cluster-panel' );
		this.panelCloseBtn = $( '.btn-close' );
		
		this.highlightText = $( '#settings-highlight-text', this.navSettings );
		this.highlightCitation = $( '#settings-highlight-citation', this.navSettings );
		this.highlightMatchingcitation = $( '#settings-highlight-matchingcitation', this.navSettings );
		this.highlightConnection = $( '#settings-show-connection', this.navSettings );
		this.documentbrowserShow = $( '#settings-show-documentbrowser', this.navSettings );
		
		this.bindEvents();
	},

	/** changes the icon of the cluster */
	iconChangeCluster : function() {
		$( '.switch-button1' ).toggleClass( 'icon-list' );
		$( '.switch-button1' ).toggleClass( 'icon-arrow-right' );
	},
	
	/** hide ALL Panels */
	hideCollapsedPanel : function() {
		this.navStatistics.removeClass( 'collapsed' );
		this.navSettings.removeClass( 'collapsed' );
		this.navDocuments.removeClass( 'collapsed' );
		this.navCluster.removeClass( 'collapsed' );
	},
	
	/** remove all active classes from the Navigation */
	removeAllActiveFromNavigation : function() {
		$( '#navigation li' ).removeClass('active');
	},

	/** binding of navigation events */
	toggleSettings : function( navLink ) {
		if ( this.navSettings.hasClass( 'collapsed' ) ) {
			this.navSettings.removeClass( 'collapsed' );
			navLink.removeClass( 'active' );
		} else {
			this.hideCollapsedPanel();
			this.navSettings.addClass( 'collapsed' );			
		}
		if ( $( '.switch-button1' ).hasClass( 'icon-forward' ) ) {
			this.iconChangeCluster();		
		};
	},

	toggleStatistics : function( navLink ) {
		if ( this.navStatistics.hasClass( 'collapsed' ) ) {
			this.navStatistics.removeClass( 'collapsed' );
			navLink.removeClass( 'active' );
		} else {
			this.hideCollapsedPanel();
			$( '#statistic-tabs a:first' ).tab('show');
			this.navStatistics.toggleClass( 'collapsed' );			
		};
		if ( $( '.switch-button1' ).hasClass( 'icon-forward' ) ) {
			this.iconChangeCluster();		
		};		
	},

	toggleDocuments : function( navLink ) {
		if ( this.navDocuments.hasClass( 'collapsed' ) ) {
			this.navDocuments.removeClass( 'collapsed' );
			navLink.removeClass( 'active' );
		} else {
			this.hideCollapsedPanel();
			this.navDocuments.toggleClass( 'collapsed' );
		}
		if ( $( '.switch-button1' ).hasClass( 'icon-forward' ) ) {
			this.iconChangeCluster();		
		};		
	},

	toggleCluster : function() {
		this.navClusterTabs = $( '#cluster-tabs' );
		this.navCluster = $( '.cluster-panel' );
		if ( this.navCluster.hasClass( 'collapsed' ) ) {
			this.navCluster.removeClass( 'collapsed' );
				this.navClusterTabs.hide();
		} else {
			this.hideCollapsedPanel();
			this.navCluster.toggleClass( 'collapsed' );
			if ( this.navCluster.hasClass( 'collapsed' ) ) {
				this.navClusterTabs.show();
			} else {
				this.navClusterTabs.hide();
			}
		}
		this.iconChangeCluster();
		if ( $( '.switch-button2' ).hasClass( 'icon-chevron-up' ) ) {
			this.iconChangeSettings();
		}
		this.removeAllActiveFromNavigation();
		cbpdp.documentCluster.enableTabs();
	},
	
	settingsChangeListener : function( element, e ) {
		var $submitButton = $( 'button[type="submit"]', element );
		if ( $submitButton.hasClass( 'disabled' ) ) {
			$submitButton.removeClass( 'disabled' );
			$submitButton.attr( 'disabled', false );
		};
	},
	
	settingsChangeHighlighMatchingCitation : function() {
		if ( this.highlightCitation.attr( 'checked' ) ) {
			this.highlightCitation.trigger( 'click' );
		}
		cbpdp.dom.documents.toggleClass( 'settings-highlight-matchingcitation-show' );

		if ( this.highlightMatchingcitation.attr( 'checked' ) ) {
			this.highlightCitation.attr( 'disabled', true );
		} else {
			this.highlightCitation.attr( 'disabled', false );
		}
	},
	
	bindEvents : function() {
		
		$( '#nav-level-1 li a' ).on( 'click', function() {
			$( '#nav-level-1 li' ).removeClass('active');
			$( this ).parent().addClass('active');
		} );
		
		$( '.settings-toggle' ).click( function () {
			cbpdp.navigation.toggleSettings( $( this ).parent() );
		} );
		
		$( '.statistics-toggle' ).click( function () {
			cbpdp.navigation.toggleStatistics( $( this ).parent() );
		} );
		
		$( '.documents-toggle' ).click( function () {
			cbpdp.navigation.toggleDocuments( $( this ).parent() );
		} );
		
		$( cbpdp.navigation.panelCloseBtn ).click( function () {
			cbpdp.navigation.hideCollapsedPanel();
			cbpdp.navigation.removeAllActiveFromNavigation();
		} );
		
		$( '#cluster' ).on('click', '.cluster-toggle', function( e ) {
			cbpdp.navigation.toggleCluster( $( this ), e );
		} );
		
		$( '#cluster' ).on('click', '#cluster-tabs a', function( e ) {
			$( this ).tab('show');
		} );
		
		$( '#cluster' ).on( 'mouseover mouseout', '#EmphasisCustomForm label' ,function( e ) {
			cbpdp.documentCluster.showTooltipsOnAlgorithmLabels( $( this ), e );
		} );
		
		$( '#cluster' ).on('click', '#btnEmphasisSave', function( e ) {
			cbpdp.documentCluster.loadEmphasisSaveForm();
		} );
		
		$( '#cluster' ).on('click', '#btnEmphasisEdit', function( e ) {
			e.preventDefault();
			cbpdp.documentCluster.loadEmphasisEditForm( $( this ) );
		} );
		
		$( '#cluster' ).on('click', '#btnEmphasisSaveCancel', function( e ) {
			cbpdp.documentCluster.cancelEmphasisSaveForm();
		} );
		
		$( '#cluster' ).on('click', '#btnEmphasisEditCancel', function( e ) {
			cbpdp.documentCluster.cancelEmphasisEditForm();
		} );
		
		$( '#cluster' ).on('change', '#ReportEmphasisId', function( e ) {
			cbpdp.documentCluster.changeLinkParams( $( this ), e );
		} );
		
		$( '#cluster' ).on('change', '#EmphasisName', function( e ) {
			cbpdp.documentCluster.checkForm( $( this ), e );
		} );
		
		$( '.cluster-toggle-sugg' ).click( function( e ) {
			cbpdp.navigation.toggleCluster( $( this ), e );
		} );

		/** if form gets modified, submit button gets enabled */
		cbpdp.navigation.navSettings.on( 'change', '.settings', function( e ) {
			cbpdp.navigation.settingsChangeListener( $( '.settings-panel' ), e )
		} );
		
		/** toggle class to show/hide text highlights */
		cbpdp.navigation.highlightText.click( function() {
			cbpdp.dom.documents.toggleClass( 'settings-highlight-text-hide' );
		} );

		/** toggle class to show/hide citation highlights */
		cbpdp.navigation.highlightCitation.click( function() {
			cbpdp.dom.documents.toggleClass( 'settings-highlight-citation-hide' );
		} );

		cbpdp.navigation.highlightMatchingcitation.click( function( e ) {
			cbpdp.navigation.settingsChangeHighlighMatchingCitation();
		} );

		cbpdp.navigation.highlightConnection.click( function() {
			cbpdp.dom.documents.toggleClass( 'settings-documentbrowser-hide-connections' );
		} );

		/** toggle class to show hide document browser */
		cbpdp.navigation.documentbrowserShow.click( function() {
			cbpdp.dom.documents.toggleClass( 'settings-documentbrowser-invisible' );
			cbpdp.dom.mask.toggleClass( 'no-browser' );
		} );
		
		
		/** enable Tabs in Statistics*/
		$( '#statistic-tabs', cbpdp.dom.navigation ).click( function() {
		  	$( this ).tab('show');
		} );
		
		$( '.algorithm-selection .btn', cbpdp.dom.navigation ).powerTip( {
			placement : 's',
			mouseOnToPopup: true,
			closeDelay: 300,
			intentPollInterval: 300
		} );
		
		$( '.algorithm-selection .btn', cbpdp.dom.navigation ).on( 'mousedown', function() {
			cbpdp.documentBrowser.changeAlgorithmSelection( $( this ) );
		} );
	}
};
