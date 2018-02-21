<!-- Cluster View -->
<div class="cluster-panel animation well">
	<div class="row">
		<div class="span5">
			<a id="btnClusterToggle" class="btn btn-inverse cluster-toggle" href="#" rel="tooltip" title="Cluster View"><i class="switch-button1 icon-white icon-list"></i> More</a>
		</div>
	</div>
	<div class="row">
		<div class="span5">
			<ul id="cluster-tabs" style="display: none;"class="nav nav-tabs">
				<li class=""><a href="#cluster-documents" data-toggle="tab">Similar Documents</a></li>
				<li class=""><a href="#cluster-suggestions" data-toggle="tab">Recommended Documents</a></li>
			    <li class="pull-right"><a href="#cluster-algorithms" data-toggle="tab">Customize</a></li>            
			</ul>
			<div class="tab-content">
				<!-- Documents Tab -->			
				<div class="tab-pane fade" id="cluster-documents">
					<table class="table cluster-table">
						<thead>
							<tr>
								<th class="cluster-score">Score</th>
								<th>Title</th>
							</tr>
						</thead>
						<tbody>
						<?php
							$i = 0;
							foreach ($documentsForCluster as $documentId => $documentScore) {	
								if($i < 6 && $documentScore > 0) {
									echo '<tr data-document-id="' . $documentId . '">';
									echo $this->Html->tag('td', $this->Html->link($documentScore, '/compare/' . $sourceId . '/' . $documentId ), array('class' => '', 'title' => 'Score'));
									echo $this->Html->tag('td', $this->Html->link($this->Text->truncate($documentsData[$documentId]['title'], 100), '/compare/' . $sourceId . '/' . $documentId), array('class' => '', 'title' => 'Document Title'));
									echo '</tr>';
								}
								$i++;
							}
						?>
						</tbody>
					</table>
				</div>
				
				<!-- Recommendations Tab -->			
				<div class="tab-pane fade" id="cluster-suggestions">
					<table class="table cluster-table">
						<thead>
							<tr>
								<th class="cluster-score">Score</th>
								<th>Title</th>
							</tr>
						</thead>
						<tbody>
						<?php
							$i = 0;
							foreach ($cpaDocumentsForCluster as $documentId => $documentScore) {	
								if($i < 6 && $documentScore > 0) {
									echo '<tr data-document-id="' . $documentId . '">';
									echo $this->Html->tag('td', $this->Html->link($documentScore, '/compare/' . $sourceId . '/' . $documentId ), array('class' => '', 'title' => 'Score'));
									echo $this->Html->tag('td', $this->Html->link($this->Text->truncate($cpaDocumentsData[$documentId]['title'], 100), array('controller' => 'reports', 'action' => 'compare', $sourceId, $documentId)), array('class' => '', 'title' => 'Document Title'));
									echo '</tr>';
								}
								$i++;
							}
						?>
						</tbody>
					</table>
				</div>
				
				<!-- Algorithms Tab -->
				<div class="tab-pane fade" id="cluster-algorithms">
					<!-- Saved Emphasis Form -->
					<div id="EmphasisIndexFormBox" class="row">
						<div class="span3">
						<?php echo $this->Form->create('Report', array('url' => '/compare/' . $sourceId, 'type' => 'post', 'id' => 'EmphasisIndexForm','class' => 'form-inline')); ?>
						<fieldset>
							<legend><?php echo __('Saved Emphasis'); ?></legend>
								<?php echo $this->Form->hidden('source_id', array('value' => $sourceId));?>
								<?php echo $this->Form->input('emphasis_id', array('class' => 'inline input-medium', 'options' => $emphasesList, 'default' => $activeEmphasisId, 'label' => false, 'div' => false));?>
						<?php echo $this->Html->tag('button', $this->Html->tag('i', ' ', array('class' => 'icon-ok')) . ' '. __('Apply'), array('type' => 'submit', 'class' => 'btn btn-mini btn-saved-emphasis pull-right'));?>
						</fieldset>
						<?php echo $this->Form->end(); ?>
						</div>
						<div class="span1">
							<div class="button-box">
							<?php echo $this->Form->postLink($this->Html->tag('i', ' ', array('class' => 'icon-trash')) . ' '. __('') , array('controller' => 'emphases', 'action' => 'delete', $sourceId, $activeEmphasisId), array('id' => 'btnEmphasisDelete', 'class' => 'btn btn-mini', 'escape' => false), __('Are you sure you want to delete  %s?', '')); ?>
						<?php echo $this->Html->link($this->Html->tag('i', ' ', array('class' => 'icon-pencil')) . ' '. __(''), array('controller' => 'emphases','action' => 'edit', $sourceId, $activeEmphasisId), array('id' => 'btnEmphasisEdit', 'class' => 'btn btn-mini', 'escape' => false)) ;?></div>
						</div>
					</div>
					<!-- Custom made Emphasis -->
					
					<?php echo $this->Form->create('Report', array('url' => '/compare/' . $sourceId, 'type' => 'post', 'id' => 'EmphasisCustomForm', 'class' => 'form-inline')); ?>
					<fieldset>
						<legend><?php echo __('Custom Emphasis'); ?></legend>
							<?php echo $this->Form->hidden('source_id', array('value' => $sourceId));?>
							<?php foreach ($algorithms as $key => $value): ?>
							<?php $class = round($algorithmPercentages[$key]) < 10 ? 'cluster-percentages small' : 'cluster-percentages'; ?>
							<?php $progressBar =  $this->Html->tag('div', '<span class="' . $class . '">' . round($algorithmPercentages[$key]) .'%' . '</span>'. $this->Html->tag('div', '' , array('class' => 'bar', 'style' => 'width: ' . $algorithmPercentages[$key] . '%')), array('class' => 'progress progress-striped'));?>
								<div class="control-group span2">
									<?php echo $this->Html->tag('label', '<span class="label">' . $value['title'] .'</span>', array('class'=>'control-label', 'title' => $value['name'], 'escape' => false));?>
									<?php $fieldValue = isset($algorithmEmphasis[$value['fieldname']]) ? $algorithmEmphasis[$value['fieldname']] : '';  ?>
									<?php echo $this->Form->input($value['fieldname'], array(
										'type' => 'number',
										'step' => 0.1,
										'min' => 0.0,
										'value' => $fieldValue,
										'placeholder' => 'coefficient', 
										'div' => false,
										'after' => '',
										'class' => 'input-small pull-right',
										'label' => false,
										)); ?>
								</div>
								<div class="span2">
									<?php echo $progressBar; ?>
								</div>
							<?php endforeach ?>
					</fieldset>
					<?php echo $this->Html->tag('button', $this->Html->tag('i', ' ', array('class' => 'icon-ok')) . ' '. __('Apply'), array('type' => 'submit', 'class' => 'btn btn-small pull-right'));?>
					<?php echo $this->Html->tag('button', $this->Html->tag('i', ' ', array('class' => 'icon-hdd')) . ' '. __('Save Settings'), array('type' => 'button', 'id' => 'btnEmphasisSave', 'class' => 'btn btn-small pull-right'));?>
					<?php echo $this->Form->end(); ?>
					<!-- Save Emphasis Form (Ajax) -->
					<div id="EmphasisSaveForm"></div>
					
					</div>
					<!-- Edit Emphasis Form (Ajax) -->
					<div id="EmphasisEditForm"></div>
					
					</div>
				</div>
			</div>
		</div>
	</div>
</div>