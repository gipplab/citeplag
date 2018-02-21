<div id="spacer"></div>
<script type="text/javascript" charset="utf-8">
	$.ajax({
		type: 'GET',
		url: cbpdp.WWW_ROOT + 'reports/renderDocuments/<?php echo $sourceId ?>/<?php echo $comparisonId ?>',
		data: {substpat_a: '<?php echo (isset($_GET['substpat_a']) ? $_GET['substpat_a'] : '') ?>', substpat_b: '<?php echo (isset($_GET['substpat_b']) ? $_GET['substpat_b'] : '') ?>'},
		beforeSend: function() {
			$( '#ajaxloader' ).show();
		},
		success : function ( data ) {
			$( '#spacer' ).replaceWith( data );
			$( '#ajaxloader' ).hide();

			cbpdp.init();
			cbpdp.documents.init();
			cbpdp.documentBrowser.init();
			cbpdp.documentCluster.init( <?php if (!empty($algorithmEmphasis)) { echo json_encode( $algorithmEmphasis ); } ?> );
			cbpdp.helpers.init()
			cbpdp.navigation.init();
			cbpdp.statistics.init();
			cbpdp.events.init();
			//$('#doc_a').annotator();
			//$('#doc_b').annotator();
		},
	});
</script>
