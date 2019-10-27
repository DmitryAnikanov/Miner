package miner.controller;

import miner.model.MinerModel;
import miner.view.MineFieldView;

public abstract class Controller {
	protected String name;
	protected MinerModel model;
	public abstract void openCell(int x, int y) throws Exception;
	public abstract void closeCell(int x, int y) throws Exception;
	public abstract void setView(MineFieldView view);
}
