<!-- Document left -->
<article id="doc_a" class="left">
	<div class="content">
		<?php
			echo $this->Html->tag('div', __('Selected Document'), array('class' => 'alert alert-info'));
			echo $this->element('document_head', array('document' => $source));
			echo $this->Reporter->renderText($source, $pattern, array('citation_replacement_pattern' => (isset($_GET['substpat_a']) && !empty($_GET['substpat_a']) ? $_GET['substpat_a'] : $this->Session->read('cbpdp.settings.citation_replacement_pattern_source'))));
		?>
	</div>
</article>

<!-- Viewport -->
<div class="document-browser">
	<aside class="left">
		<span class="spacer-bg-top"></span>
		<span class="spacer-bg-body"></span>
		<div class="overlay-viewport"></div>
	</aside>
	<aside class="right">
		<span class="spacer-bg-top"></span>
		<span class="spacer-bg-body"></span>
		<div class="overlay-viewport"></div>
	</aside>
	<canvas id="canvas"></canvas>
</div>
<!-- Document right -->
<article id="doc_b" class="right">
	<div class="content">
		<?php
			echo $this->Html->tag('div', __('Similar Document'), array('class' => 'alert alert-info'));
			echo $this->element('document_head', array('document' => $comparison));
			echo $this->Reporter->renderText($comparison, $pattern, array('citation_replacement_pattern' => (isset($_GET['substpat_b']) && !empty($_GET['substpat_b']) ? $_GET['substpat_b'] : $this->Session->read('cbpdp.settings.citation_replacement_pattern_comparison'))));
		?>
	</div>
</article>
<script type="text/javascript" charset="utf-8">
	cbpdp.init();
	cbpdp.documents.init();
	cbpdp.documentBrowser.init();
	cbpdp.helpers.init()
	cbpdp.navigation.init();
	cbpdp.statistics.init();
	cbpdp.events.init();
	$('.settings-panel #ReportUploadForm .settings').parent().hide();
	$('.settings-panel #ReportUploadForm .submit').hide();
</script>