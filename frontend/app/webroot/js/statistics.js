
/**
 * initializes a dummy chart from google
 * TODO grab some relevant data from the documents and render google charts with them (GuttenbergBar and Histogram so far)
 * 
*/
cbpdp.statistics = {
	/**
	 * Set chart settings for both statistics
	 */
	settings : {
		colors : [ '#FF7FB0', '#CAF562', '#7F97FF', '#FF9C7E', '#F3FF7E', '#62F5C8', '#FFF17E', '#7FCAFF', '#FFBD7E', '#A77FFF', '#E77FFF', '#FFD77E' ],
		width: 970, 
		height: 350,
		hAxis: { titleTextStyle : { color : '#333' }, textStyle : { color : '#333' } },
		vAxis: { titleTextStyle : { color : '#333' }, textStyle : { color : '#333' } },
		backgroundColor : { fill : 'none', stroke : 'none', strokeWidth : 10 },
		chartArea: { height : 200, width : 600 },
		tooltip : { trigger : 'none' }
	},

	stackedTextPlagChart : undefined,
	stackedCitePlagChart : undefined,
	docA : undefined,
	textMatchPosData : undefined,
	citationMatchPosData : undefined,
	documentText : undefined,
	documentLength : undefined,
	DOCUMENT_PARTS : undefined,
	
	init : function() {
		// Put DOM elements with id chart1 & chart2 in objects
		this.stackedTextPlagChart = $( '#stackedTextPlagChart' );
		this.stackedCitePlagChart = $( '#stackedCitePlagChart' );
		this.drawStackedTextPlagChart();
		this.drawStackedCitePlagChart();
	},

	drawStackedTextPlagChart : function() {
		// google.setOnLoadCallback( function() {
			var name = 'left';
			var $article = $( 'article.' + name, cbpdp.dom.documents );
			var $documentBrowserForArticle = $( '.' + name, cbpdp.dom.documentBrowser );
			
			var markArray = new Array( $( 'section', $documentBrowserForArticle ).length );
			for (var i=0; i < markArray.length; i++) {
				markArray[ i ] = [ ( i + 1 ).toString() ];
			};
			
			$.each( $( 'mark', $article ), function( index, value ) {
				var $item = $( value );
				var pageIndex = parseInt ( $item.position().top / ( cbpdp.LINES_PER_PAGE * cbpdp.LINE_HEIGHT ) );
				if ( markArray[ pageIndex ] === undefined ) {
					markArray[ pageIndex ] = [ pageIndex.toString() ];
				};
				markArray[ pageIndex ].push( $item.data( 'length' ) );
			});
			
			var longestArray = cbpdp.helpers.findLongestSubArray( markArray );
			markArray = cbpdp.statistics.fillUpSubArray( markArray, longestArray );
			var data = new google.visualization.DataTable();
			data.addColumn( 'string', 'Page' );
			for ( var i=0; i < longestArray; i++ ) {
				data.addColumn( 'number', 'Score' );
			};
			data.addRows( markArray );
			var options = {
				// colors : [ '#FF0000' ],
				hAxis: { title : 'Pages in Selected Document' },
				vAxis: { title : 'Stacked Identical Text Lengths' },
				isStacked : true,
				isHtml : true,
				legend : 'none'
			};

			var chart = new google.visualization.ColumnChart( document.getElementById( 'stackedTextPlagChart' ) );
			chart.draw( data, $.extend( true, {}, cbpdp.statistics.settings, options ) );
		// } );
	},

	drawStackedCitePlagChart : function() {
		// google.setOnLoadCallback( function() {
			var name = 'left';
			var $article = $( 'article.' + name, cbpdp.dom.documents );
			var $documentBrowserForArticle = $( '.' + name, cbpdp.dom.documentBrowser );
			
			var sections = $( 'section', $documentBrowserForArticle );
			
			// the procedure id the chart is rendered for
			var procedure = 71;
			var pages = $( 'section', $documentBrowserForArticle ).length;
			var citationCountsForPages = new Array( pages );
			var matchingCitesCountForPages = new Array( pages );
			
			$.each( $('cite:visible', $article ), function( index, value ) {
				var $item = $( value );
				var pageIndex = parseInt ( $item.position().top / ( cbpdp.LINES_PER_PAGE * cbpdp.LINE_HEIGHT ) );
				
				if ( citationCountsForPages[ pageIndex ] === undefined ) {
					citationCountsForPages[ pageIndex ] = 0;
				};
				
				if ( matchingCitesCountForPages[ pageIndex ] === undefined ) {
					matchingCitesCountForPages[ pageIndex ] = 0;
				};
				
				citationCountsForPages[ pageIndex ] += 1;
				
				var identifier = $item.data( 'identifier' );
				if ( identifier !== undefined ) {
					for ( var i=0; i < identifier.length; i++ ) {
						if ( identifier[ i ].procedure == procedure ) {
							matchingCitesCountForPages[ pageIndex ] += 1
							break;
						};
					};	
				};
			});
			
			var citeArray = new Array( pages );
			for ( var i = 0; i < pages; i++ ) {
				citeArray[ i ] = [ ( i + 1 ).toString() ];
				citeArray[ i ].push( ( matchingCitesCountForPages[ i ] ) );
				citeArray[ i ].push( ( citationCountsForPages[ i ] - matchingCitesCountForPages[ i ] ) );
			};

			var data = new google.visualization.DataTable();
			data.addColumn( 'string', 'Page' );
			data.addColumn( 'number', 'Identical Citations' );
			data.addColumn( 'number', 'Overall' );
			data.addRows( citeArray );
			
			var options = {
				colors : [ '#1C4587', '#CCCCCC' ],
				hAxis: { title : 'Pages in Selected Document' },
				vAxis: { title : 'Quantity' },
				isStacked : true,
				isHtml : true,
				// legend : 'none'
			};
			
			var chart = new google.visualization.ColumnChart( document.getElementById( 'stackedCitePlagChart' ) );
			chart.draw( data, $.extend( true, {}, cbpdp.statistics.settings, options ) );
		// } );
	},
	
	fillUpSubArray : function( array, size ) {
		for ( var i=0; i < array.length; i++ ) {
			for ( var j = 1; j <= size; j++ ) {
				if ( array[ i ] === undefined ) {
					array[ i ] = new Array( size );
				};
				if ( array[ i ][ j ] === undefined ) {
					array[ i ][ j ] = 0;
				};
			};
		};
		return array;
	}
};
