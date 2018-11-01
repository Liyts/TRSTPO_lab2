public class Main {
	int honeyPot = 0, maxHoney;
	Bee[] bees;
	Bear bear;
	Object mutex = new Object();

	public Main(int maxHoney, int countBee) {
		this.maxHoney = maxHoney;
		bees = new Bee[countBee];
		for (int i = 0; i < bees.length; i++) {
			bees[i] = new Bee(i);
		}
		bear = new Bear();
	}

	class Bee extends Thread {
		int countBee;

		public Bee(int countBee) {
			this.countBee = countBee;
		}

		@Override
		public void run() {
			honeyPot++;
			System.out.println("Пчелка №" + countBee + "принесла мёд");
			if (honeyPot == maxHoney) {
				bear = new Bear();
				bear.start();
			}
		}
	}

	public void goBee() {
		for (int i = 0; i < bees.length; i++) {
			synchronized (mutex) {
				bees[i].run();
				try {
					bees[i].sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class Bear extends Thread {
		@Override
		public void run() {
			honeyPot = 0;
			System.out.println("Упс... медведь съел весь мёд");
		}
	}

	public static void main(String[] args) {
		Main main = new Main(10, 5);
		while (true) {
			main.goBee();
		}
	}
}
