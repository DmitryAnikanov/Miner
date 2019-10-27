package miner.model.mines;

public class MineFactory {
	
	private AbstractMineCreator mineCreator = null;
	
	public MineFactory() {
		
	}
	
	public Mine createMine(Class mineClass) {
        if (mineClass.equals(SimpleMine.class) ) {
        	mineCreator = new SimpleMineCreator();
        } 
        return mineCreator.create(null);
    }
}
