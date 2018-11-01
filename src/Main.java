import java.util.Scanner;

public class Main {
	int honeyPot = 0, maxHoney;
	Bee[] bees;
	Bear bear;
	public boolean check = true;
	Object mutex = new Object();

	public Main(int maxHoney, int countBee) {
		this.maxHoney = maxHoney;
		mutex = check;
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
			System.out.println("Пчелка №" + (countBee+1) + " принесла мёд");
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
					bees[i].sleep(1500);
				} catch (InterruptedException e) {
					return;
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
		Thread decor = new Thread() {
			@Override
			public void run() {
				while (main.check) {
					main.goBee();
				}
			}
		};
		decor.start();
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		main.check = false;
		decor.interrupt();
		System.out.println("До встречи!");
	}
}
