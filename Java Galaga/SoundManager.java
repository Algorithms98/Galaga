public class SoundManager {
	public final Sound playerShot;
	public final Sound enemyShot;
	public final Sound loseLife;
	public final Sound playerExplosion;
	public final Sound enemyExplosion;

	public SoundManager() {
		playerShot = new Sound("Sounds//pShot.wav");
		enemyShot = new Sound("Sounds//eShot.wav");
		loseLife = new Sound("Sounds//loseLife.wav");
		playerExplosion = new Sound("Sounds//pExplosion.wav");
		enemyExplosion = new Sound("Sounds//eExplosion.wav");
	}
}
