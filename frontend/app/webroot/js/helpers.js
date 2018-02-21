/**
 *
 *
 */
cbpdp.helpers = {
	
	init : function() {
		
	},

	getMatch : function( e, pattern ) {
		for ( i = 0; i < e.classList.length; i++ ) {
			if ( e.classList[i].match( pattern ) ) {
				return e.classList[i];
			}
		}
		return false;
	},
	
	showHint : function( message ) {
		var $hint = $( '#hint' );
		$( '.content', $hint ).text( message );
		$hint.removeClass( 'invisible' );
	},
	
	hideHint : function() {
		if ( !$( '#hint' ).hasClass( 'invisible' ) ) {
			$( '#hint' ).addClass( 'invisible' );
		}
	},
	
	getFormattedTime : function(year, month) {
		var result = "";
		if ( year ) {
			result += year;
		};
		if ( month ) {
			result += '/' + month;
		};
		return result;
	},
	
	stringExplode : function( string, delimiter ) {
		string = string.toString();
		if ( string.indexOf( delimiter ) == -1 ) {
			return [ string ];
		} else {
			return string.split( delimiter );
		}
	},
	
	findLongestSubArray : function( array ) {
		var longestArray = 0;			
		for (var i=0; i < array.length; i++) {
			longestArray = ( $( array[ i ] ).length > longestArray ) ? $( array[ i ] ).length : longestArray;
		};
		return longestArray;
	}

}
