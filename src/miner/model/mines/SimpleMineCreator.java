package miner.model.mines;

public class SimpleMineCreator extends AbstractMineCreator{

	@Override
	public Mine create(Object context) {
		return new SimpleMine();
	}

}
