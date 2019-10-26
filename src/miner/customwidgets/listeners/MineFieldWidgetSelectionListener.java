package miner.customwidgets.listeners;

import java.util.EventListener;

import miner.customwidgets.events.MineFieldWidgetSelectionEvent;


public interface MineFieldWidgetSelectionListener extends EventListener {

    public void mineFieldWidgetSelected(MineFieldWidgetSelectionEvent imageButtonSelectionEvent);
}