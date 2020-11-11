public class SoundManager {
	public final Sound playerShot;
	public final Sound loseLife;
	public final Sound playerExplosion;
	public final Sound enemyExplosion;
	public final Sound enemyExplosion2;
	public final Sound enemyExplosion3;
	public final Sound enemyFlyDown;
	public final Sound breathingDown;
	public final Sound menuSound;

	public SoundManager() {
		playerShot = new Sound("Sounds//pShot.wav");
		loseLife = new Sound("Sounds//loseLife.wav");
		playerExplosion = new Sound("Sounds//pExplosion.wav");
		enemyExplosion = new Sound("Sounds//eExplosion.wav");
		enemyExplosion2 = new Sound("Sounds//eExplosion2.wav");
		enemyExplosion3 = new Sound("Sounds//eExplosion3.wav");
		enemyFlyDown = new Sound("Sounds//eFlyDown.wav");
		breathingDown = new Sound("Sounds/breathingDown.wav");
		menuSound = new Sound("Sounds/Menu.wav");
	}
	public void stopAll()
	{
		playerShot.stop();
		loseLife.stop();
		playerExplosion.stop();
		enemyExplosion2.stop();
		enemyExplosion3.stop();
		enemyFlyDown.stop();
		breathingDown.stop();
	}
}
