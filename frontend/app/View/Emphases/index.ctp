<div class="emphases index">
	<h2><?php echo __('Emphases'); ?></h2>
	<table cellpadding="0" cellspacing="0">
	<tr>
			<th><?php echo $this->Paginator->sort('id'); ?></th>
			<th><?php echo $this->Paginator->sort('created'); ?></th>
			<th><?php echo $this->Paginator->sort('modified'); ?></th>
			<th><?php echo $this->Paginator->sort('name'); ?></th>
			<th><?php echo $this->Paginator->sort('coefficient_bc'); ?></th>
			<th><?php echo $this->Paginator->sort('coefficient_cc'); ?></th>
			<th><?php echo $this->Paginator->sort('coefficient_gct'); ?></th>
			<th><?php echo $this->Paginator->sort('coefficient_lccs'); ?></th>
			<th><?php echo $this->Paginator->sort('coefficient_encoplot'); ?></th>
			<th><?php echo $this->Paginator->sort('default'); ?></th>
			<th class="actions"><?php echo __('Actions'); ?></th>
	</tr>
	<?php
	foreach ($emphases as $emphasis): ?>
	<tr>
		<td><?php echo h($emphasis['Emphasis']['id']); ?>&nbsp;</td>
		<td><?php echo h($emphasis['Emphasis']['created']); ?>&nbsp;</td>
		<td><?php echo h($emphasis['Emphasis']['modified']); ?>&nbsp;</td>
		<td><?php echo h($emphasis['Emphasis']['name']); ?>&nbsp;</td>
		<td><?php echo h($emphasis['Emphasis']['coefficient_bc']); ?>&nbsp;</td>
		<td><?php echo h($emphasis['Emphasis']['coefficient_cc']); ?>&nbsp;</td>
		<td><?php echo h($emphasis['Emphasis']['coefficient_gct']); ?>&nbsp;</td>
		<td><?php echo h($emphasis['Emphasis']['coefficient_lccs']); ?>&nbsp;</td>
		<td><?php echo h($emphasis['Emphasis']['coefficient_encoplot']); ?>&nbsp;</td>
		<td><?php echo h($emphasis['Emphasis']['default']); ?>&nbsp;</td>
		<td class="actions">
			<?php echo $this->Html->link(__('View'), array('action' => 'view', $emphasis['Emphasis']['id'])); ?>
			<?php echo $this->Html->link(__('Edit'), array('action' => 'edit', $emphasis['Emphasis']['id'])); ?>
			<?php echo $this->Form->postLink(__('Delete'), array('action' => 'delete', $emphasis['Emphasis']['id']), null, __('Are you sure you want to delete # %s?', $emphasis['Emphasis']['id'])); ?>
		</td>
	</tr>
<?php endforeach; ?>
	</table>
	<p>
	<?php
	echo $this->Paginator->counter(array(
	'format' => __('Page {:page} of {:pages}, showing {:current} records out of {:count} total, starting on record {:start}, ending on {:end}')
	));
	?>	</p>

	<div class="paging">
	<?php
		echo $this->Paginator->prev('< ' . __('previous'), array(), null, array('class' => 'prev disabled'));
		echo $this->Paginator->numbers(array('separator' => ''));
		echo $this->Paginator->next(__('next') . ' >', array(), null, array('class' => 'next disabled'));
	?>
	</div>
</div>
<div class="actions">
	<h3><?php echo __('Actions'); ?></h3>
	<ul>
		<li><?php echo $this->Html->link(__('New Emphase'), array('action' => 'add')); ?></li>
	</ul>
</div>
