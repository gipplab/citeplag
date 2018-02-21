<div class="emphases view">
<h2><?php  echo __('Emphasis'); ?></h2>
	<dl>
		<dt><?php echo __('Id'); ?></dt>
		<dd>
			<?php echo h($emphasis['Emphasis']['id']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Created'); ?></dt>
		<dd>
			<?php echo h($emphasis['Emphasis']['created']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Modified'); ?></dt>
		<dd>
			<?php echo h($emphasis['Emphasis']['modified']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Name'); ?></dt>
		<dd>
			<?php echo h($emphasis['Emphasis']['name']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Coefficient Bc'); ?></dt>
		<dd>
			<?php echo h($emphasis['Emphasis']['coefficient_bc']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Coefficient Cc'); ?></dt>
		<dd>
			<?php echo h($emphasis['Emphasis']['coefficient_cc']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Coefficient Gct'); ?></dt>
		<dd>
			<?php echo h($emphasis['Emphasis']['coefficient_gct']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Coefficient Lccs'); ?></dt>
		<dd>
			<?php echo h($emphasis['Emphasis']['coefficient_lccs']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Default'); ?></dt>
		<dd>
			<?php echo h($emphasis['Emphasis']['default']); ?>
			&nbsp;
		</dd>
	</dl>
</div>
<div class="actions">
	<h3><?php echo __('Actions'); ?></h3>
	<ul>
		<li><?php echo $this->Html->link(__('Edit Emphasis'), array('action' => 'edit', $emphasis['Emphasis']['id'])); ?> </li>
		<li><?php echo $this->Form->postLink(__('Delete Emphasis'), array('action' => 'delete', $emphasis['Emphasis']['id']), null, __('Are you sure you want to delete # %s?', $emphasis['Emphasis']['id'])); ?> </li>
		<li><?php echo $this->Html->link(__('List Emphases'), array('action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('New Emphasis'), array('action' => 'add')); ?> </li>
	</ul>
</div>
