<!DOCTYPE html>
<html>
<head>
	<?php echo $this->Html->charset(); ?>
    <title>
		<?php echo $title_for_layout; ?>
	</title>
	<?php
		echo $this->Html->meta('icon', $this->webroot . 'img/sciplore_logo.ico');
		echo $this->Html->meta(array('name' => 'robots', 'content' => 'noindex, nofollow'));
		echo $this->Html->meta(array('name' => 'viewport', 'content' => 'width=device-width, initial-scale=1.0'));
		echo $this->fetch('meta');
		echo $this->Html->css(array('bootstrap-blue.css', 'bootstrap-responsive.css', 'bootstrap-addons.css', 'annotator.min.css', 'jquery.powertip', 'custom.css'));
		$this->Html->script(array(
			'libs/jquery-1.8.3.min',
			'https://www.google.com/jsapi',
			'libs/bootstrap.js',
			'libs/jquery.powertip-1.1.0.min',
			'libs/jquery.scrollTo-1.4.3.1-min',
			'libs/jquery.cookie',
			'libs/annotator-full.min',
			'libs/ejs',
			'app',
			'document_browser',
			'document_cluster',
			'documents',
			'events',
			'helpers',
			'navigation',
			'statistics',
		), array('inline' => false));
		echo $this->fetch('script');
	?>
	<script type="text/javascript" charset="utf-8">
		google.load('visualization', '1.0', {'packages':['corechart']});	
	</script>
</head>
<body>
	<div id="modal" class="modal hide fade"></div>
	<?php echo $this->element('navigation'); ?>

	<div id="ajaxloader">
	<div class="ajaxload-img"></div>		
		<div class="progress progress-striped active">
		  <div class="bar" style="width: 100%;"></div>
		</div>
	</div>
	<div id="cluster"></div>
	<div id="wrapper" class="container">
		<div id="hint" class="alert alert-info invisible">
			<button type="button" class="close" data-dismiss="alert">x</button>
			<p class="content"></p>
		</div>
		<div class="document-wrapper">
			<div class="mask"></div>
			<div class="mask mask-right"></div>
			<?php
				$documentSettings = array();
				if (!$this->Session->read('cbpdp.settings.highlight_text')) array_push($documentSettings, 'settings-highlight-text-hide');
				if (!$this->Session->read('cbpdp.settings.highlight_citation')) array_push($documentSettings, 'settings-highlight-citation-hide');
				if ($this->Session->read('cbpdp.settings.highlight_matchingcitation')) array_push($documentSettings, 'settings-highlight-matchingcitation-show');
				if (!$this->Session->read('cbpdp.settings.show_connection')) array_push($documentSettings, 'settings-documentbrowser-hide-connections');
				if (!$this->Session->read('cbpdp.settings.show_documentbrowser')) array_push($documentSettings, 'settings-documentbrowser-invisible');
			?>
	  		<div class="document <?php echo implode(' ', $documentSettings); ?>">
		        <?php echo $this->Session->flash(); ?>
				<?php echo $this->fetch('content'); ?>
			</div>
		</div>
	</div>
<!-- Piwik -->
<script type="text/javascript"> 
  var _paq = _paq || [];
  _paq.push(['trackPageView']);
  _paq.push(['enableLinkTracking']);
  (function() {
    var u=(("https:" == document.location.protocol) ? "https" : "http") + "://stats.sciplore.org//";
    _paq.push(['setTrackerUrl', u+'piwik.php']);
    _paq.push(['setSiteId', 1]);
    var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0]; g.type='text/javascript';
    g.defer=true; g.async=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
  })();

</script>
<noscript><p><img src="http://stats.sciplore.org/piwik.php?idsite=1" style="border:0" alt="" /></p></noscript>
<!-- End Piwik Code -->
</body>
</html>
