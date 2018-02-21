<?php App::import('Model', 'Pattern'); ?>
<div id="navigation">
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<?php if ($this->params['controller'] == 'reports' && ($this->params['action'] == 'compare' || $this->params['action'] == 'upload')) { ?>
				<ul id="nav-level-1" class="nav">
					<li>
						<a href="#" class="documents-toggle"><i class="icon-white icon-file"></i> <?php echo __('Open'); ?></a>
					</li>
					<li>
						<a href="#" class="statistics-toggle"><i class="icon-white icon-tasks"></i> <?php echo __('Statistics'); ?></a>
					</li>
					<li>
						<a href="#" class="settings-toggle"><i class="switch-button2 icon-white icon-cog"></i> <?php echo __('Settings'); ?></a>
					</li>
					<li>
						<?php echo $this->Html->link( $this->Html->tag('i', '', array('class' => 'icon-white icon-question-sign')).__(' Help', true), '/js/templates/pages/help.ejs', array( 'data-modal' => 'modal', 'escape' => false ) ); ?>
					</li>
				</ul>
				<header class="algorithm-selection">
					<div class="btn-group" data-toggle="buttons-radio">
						<a rel="tooltip" title="<?php echo __('Bibliographic Coupling', true); ?>, for details on this algorithm see <a href='<?php echo $this->Html->url('/js/templates/pages/bc.ejs'); ?>' data-modal='modal'>help</a>" data-procedure="<?php echo implode('_', array(Pattern::TYPE_BIBLIOGRAPHIC_COUPLING)); ?>" class="btn btn-inverse active">BC</a>
						<a rel="tooltip" title="<?php echo __('Citation Chunking', true); ?>, for details on this algorithm see <a href='<?php echo $this->Html->url('/js/templates/pages/cc.ejs'); ?>' data-modal='modal'>help</a>" data-procedure="<?php  echo implode('_', array(Pattern::TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE)); ?>" class="btn btn-inverse">CC</a>
						<a rel="tooltip" title="<?php echo __('Greedy Citation Tiling', true); ?>, for details on this algorithm see <a href='<?php echo $this->Html->url('/js/templates/pages/gct.ejs'); ?>' data-modal='modal'>help</a>" data-procedure="<?php echo implode('_', array(Pattern::TYPE_GREEDY_CITATION_TILING_ALL)); ?>" class="btn btn-inverse">GCT</a>
						<a rel="tooltip" title="<?php echo __('Longest Common Citation Sequence', true); ?>, for details on this algorithm see <a href='<?php echo $this->Html->url('/js/templates/pages/lccs.ejs'); ?>' data-modal='modal'>help</a>" data-procedure="<?php echo implode('_', array(Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE)); ?>" class="btn btn-inverse">LCCS</a>
						<a rel="tooltip" title="<?php echo __('Longest Common Citation Sequence Distinct', true); ?>, for details on this algorithm see <a href='<?php echo $this->Html->url('/js/templates/pages/lccs.ejs'); ?>' data-modal='modal'>help</a>" data-procedure="<?php echo implode('_', array(Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE_DISTINCT)); ?>" class="btn btn-inverse">LCCS Dist.</a>
					</div>
				</header>
				<?php } ?>
				<ul class="nav pull-right">
					<li>
						<?php echo $this->Html->link( $this->Html->tag('i', '', array('class' => 'icon-white icon-info-sign')).__( ' About', true ), '/js/templates/pages/about.ejs', array( 'data-modal' => 'modal', 'escape' => false ) ); ?>
					</li>
					<li>
						<a href="http://www.isg.uni-konstanz.de" title="Information Science Group, Univ. of Konstanz" id="logo" target="_blank">
							<?php echo $this->Html->image('isg_logo.png', array('alt' => 'ISG Logo'))?>
						</a>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<!-- Settings -->
	<div class="settings-panel animation well">
		<div class="row">
			<?php echo $this->Form->create('Report', array('url' => array('controller' => 'reports', 'action' => 'setSettings'), 'type' => 'post')); ?>
				<div class="span5">
					<fieldset>
						<?php
							echo $this->Html->tag('legend', __('Display Settings'));
    	
							echo $this->Form->input('source_id', array('type' => 'hidden', 'value' => (isset($sourceId) ? $sourceId : '')));
							echo $this->Form->input('comparison_id', array('type' => 'hidden', 'value' => (isset($comparisonId) ? $comparisonId : '')));
    	
							echo $this->Form->input('highlight_text', array('type' => 'checkbox', 'hiddenField' => false, 'id' => 'settings-highlight-text', 'label' => __('Show text highlights'), 'checked' => $this->Session->read('cbpdp.settings.highlight_text')));
							echo $this->Form->input('highlight_citation', array('type' => 'checkbox', 'hiddenField' => false, 'id' => 'settings-highlight-citation', 'label' => __('Highlight citations'), 'checked' => $this->Session->read('cbpdp.settings.highlight_citation')));
							echo $this->Form->input('highlight_matchingcitation', array('type' => 'checkbox', 'hiddenField' => false, 'id' => 'settings-highlight-matchingcitation', 'label' => __('Highlight matching citations only'), 'checked' => $this->Session->read('cbpdp.settings.highlight_matchingcitation')));
							echo $this->Form->input('show_connection', array('type' => 'checkbox', 'hiddenField' => false, 'id' => 'settings-show-connection', 'label' => __('Show connections between matching citations'), 'checked' => $this->Session->read('cbpdp.settings.show_connection')));
							echo $this->Form->input('show_documentbrowser', array('type' => 'checkbox', 'hiddenField' => false, 'id' => 'settings-show-documentbrowser', 'label' => __('Show/hide document browser'), 'checked' => $this->Session->read('cbpdp.settings.show_documentbrowser')));
						?>
					</fieldset>
				</div>
				<div class="span5">
					<fieldset class="settings">
						<?php
							echo $this->Html->tag('legend', __('Text Rendering'));
							echo $this->Form->input('encoplot_keylength', array('div' => false, 'placeholder' => 10, 'label' => __('minimum character-match length'), 'value' => $this->Session->read('cbpdp.settings.encoplot_keylength')));
							echo $this->Form->input('citation_replacement_pattern_source', array('div' => false, 'placeholder' => '%', 'label' => __('replacement pattern for selected document'), 'value' => $this->Session->read('cbpdp.settings.citation_replacement_pattern_source')));
							echo $this->Form->input('citation_replacement_pattern_comparison', array('div' => false, 'placeholder' => '%', 'label' => __('replacement pattern for similar document'), 'value' => $this->Session->read('cbpdp.settings.citation_replacement_pattern_comparison')));
						?>
					</fieldset>
				</div>
			<?php echo $this->Form->end(array('label' => __('Apply Settings'), 'class' => 'btn btn-inverse btn-primary pull-right')); ?>
		</div>
		<button type="button" title="Close" class="btn-close btn"><i class="icon-chevron-up"></i></button>
	</div>
		
	
	<!-- Statistics-->
	
	<div class="statistics-panel animation well">
		<div class="row">
			<div class="span10 tabbable">
				<ul id="statistic-tabs" class="nav nav-tabs">
					<li><a href="#stackedTextPlagChart" data-toggle="tab">Stacked Text Plagiarism Lengths</a></li>
				    <li><a href="#stackedCitePlagChart" data-toggle="tab">Biblographic Coupling Strength</a></li>            
				</ul>
				<div class="tab-content">
					<div class="tab-pane fade" id="stackedTextPlagChart"></div>
					<div class="tab-pane fade" id="stackedCitePlagChart"></div>
				</div>
			</div>
		</div>
		<button type="button" title="Close" class="btn-close btn"><i class="icon-chevron-up"></i></button>
	</div>
	
	<!-- Documents -->
	
	<div class="documents-panel animation well form-horizontal">
		<?php echo $this->element('document_select'); ?>
		<button type="button" title="Close" class="btn-close btn"><i class="icon-chevron-up"></i></button>
	</div>

</div>
