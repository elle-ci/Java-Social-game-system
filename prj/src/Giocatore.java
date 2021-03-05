import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
//progetto backup 02.06.19
public class Giocatore extends Center {
	int giorno_nascita;
	int giorni_attivi;
	int giorno_morte;
	int figli;
	double umore;
	Carattere carattere;
	HashMap<Integer, Integer> vocabolario;
	HashMap<Integer, LinkedList<Integer>> dizionario_out;
	HashMap<Giocatore, ValoreInterazione> interazioni;
	LinkedList<Giocatore> conoscenti;
	LinkedList<String> ListaCarattereDominante;
	String nome;
	String cognome;


	public Giocatore(LinkedList<String> lista_caratteri) {
		//costruttore di un oggetto di tipo di tipo giocatore
		if (p) {
			giorno_nascita = giorno;
			giorni_attivi = 0;
			giorno_morte = (random.nextInt(20)+EtaMassima);
			umore = UmoreIniziale;
			figli = 0;
			System.out.println("\nInserisci il tuo nome: ");
			while (inputScanner.hasNextInt()) {
				System.out.println("Il nome non può essere un numero! Come lo vuoi chiamare? ");
				inputScanner.next();
			}
			nome = inputScanner.next();
			System.out.println("Inserisci il tuo cognome: ");
			while (inputScanner.hasNextInt()) {
				System.out.println("Il cognome non può essere un numero! Come lo vuoi chiamare? ");
				inputScanner.next();
			}
			cognome = inputScanner.next();
			System.out.println();
			vocabolario = new HashMap<Integer, Integer>();
			dizionario_out = new HashMap<Integer, LinkedList<Integer>>();
			interazioni = new HashMap<Giocatore, ValoreInterazione>();
			conoscenti = new LinkedList<Giocatore>();
			carattere = new Carattere(lista_caratteri);
			ListaCarattereDominante = new LinkedList<String>();
			CompilaListe();
			p=false;
		}
		else {
			giorno_nascita = giorno;
			giorni_attivi = 0;
			giorno_morte = (random.nextInt(20)+EtaMassima);
			umore = UmoreIniziale;
			figli = 0;
			nome = Nomi.get(random.nextInt(Nomi.size()));
			cognome = Cognomi.get(random.nextInt(Cognomi.size()));
			vocabolario = new HashMap<Integer, Integer>();
			dizionario_out = new HashMap<Integer, LinkedList<Integer>>();
			interazioni = new HashMap<Giocatore, ValoreInterazione>();
			conoscenti = new LinkedList<Giocatore>();
			carattere = new Carattere(lista_caratteri);
			ListaCarattereDominante = new LinkedList<String>();
			CompilaListe();
		}
	}

	static class ValoreInterazione {
		double importanza;
		int data_ultimo_mex_ricevuto;
		LinkedList<Integer> storico;

		public ValoreInterazione() {
			this.importanza = ImportanzaIniziale;
			this.data_ultimo_mex_ricevuto = giorno-1;
			this.storico = new LinkedList<Integer>();
		}
	}

	public void AggiornaImportanzaConoscente(Giocatore conoscente) throws InterruptedException { //aggiorna l'importanza ogni 5 giorni
		if (interazioni.get(conoscente).storico.size() >= 5 && interazioni.get(conoscente).storico.size() % 5 == 0) {
			double oldimportanza = 0;
			if (slow) oldimportanza = interazioni.get(conoscente).importanza;
			carattere.AggiornaImportanza(conoscente, this);
			if (slow) {
				System.out.println(nome + " " + cognome + " aggiorna l'importanza di " + conoscente.nome + " " + conoscente.cognome + ", da " + Round(oldimportanza) + " a " + Round(interazioni.get(conoscente).importanza));
				Thread.sleep(sleep);
			}
		}
	}

	public boolean AggiornaGiocatore() throws InterruptedException {
		if (umore>=100) {
			if (figli < LimiteFigli) Nascita();
			if (io==this && figli == LimiteFigli) System.out.println("\nSei molto felice, ma l'eta' avanza e non puoi più avere figli! ");
			umore -= 50;
		}
		else if (umore<=0 || giorni_attivi==giorno_morte) {Morte(); return false;}
		return true;
	}

	private void Morte() throws InterruptedException {
		if (this.carattere.carattere.equals("player")) {
			if (!MortiViventi.contains(this)) {
				if (umore<=0) System.out.println("Mi dispiace " + this.nome + " " + this.cognome + ", sei morto prematuramente di tristezza! Il gioco termina qui." + " (Umore: " + Round(umore) + ")");
				else System.out.println("Mi dispiace " + this.nome + " " + this.cognome + ", la tua ora e' giunta, sei morto! Il gioco termina qui. " +  " (Umore: " + Round(umore) + ") | Giorni: " + giorno_morte );
			}
			isalive=false;
		}
		if (slow && !MortiViventi.contains(this)) {
			System.out.println(nome + " " + cognome + " e' morto");
			Thread.sleep(sleep);
		}
		MortiViventi.add(this);
	}

	private void Nascita() throws InterruptedException {
		//crea un altro giocatore che ha come vocabolario e dizionario_out quelli del padre e conosce il padre
		Giocatore figlio;
		if (slow) {
			System.out.println(nome + " " + cognome + " fa nascere un figlio");
			Thread.sleep(sleep);
		}
		if (cimitero.isEmpty()) {
			if (carattere.carattere.equals("player")) figlio = new Giocatore(caratteri);
			else figlio = new Giocatore(ListaCarattereDominante);
		}
		else {
			figlio = cimitero.pollFirst();
			if (carattere.carattere.equals("player")) {
				Collections.shuffle(caratteri);
				figlio.carattere.carattere = caratteri.get(0);
				figlio.giorno_nascita = giorno;
				figlio.giorni_attivi = 0;
				figlio.giorno_morte = (random.nextInt(20)+EtaMassima);
				figlio.umore = UmoreIniziale;
				figlio.figli = 0;
				System.out.println("Complimenti, hai avuto un figlio! Come lo vuoi chiamare? ");
				while (inputScanner.hasNextInt()) {
					System.out.println("Il nome non può essere un numero! Come lo vuoi chiamare? ");
					inputScanner.next();
				}
				figlio.nome=inputScanner.next();
				figlio.cognome = this.cognome;
				System.out.println();
				System.out.println("Ora " + figlio.nome + " " + cognome + " fa parte della società! \n");
				figlio.vocabolario.clear();
				figlio.dizionario_out.clear();
				figlio.interazioni.clear();
				figlio.conoscenti.clear();
				figlio.ListaCarattereDominante.clear();
				figlio.CompilaListe();
			}
			else {
				Collections.shuffle(ListaCarattereDominante);
				figlio.carattere.carattere = ListaCarattereDominante.get(0);
				figlio.giorno_nascita = giorno;
				figlio.giorni_attivi = 0;
				figlio.giorno_morte = (random.nextInt(20)+EtaMassima);
				figlio.umore = UmoreIniziale;
				figlio.figli = 0;
				figlio.nome = Nomi.get(random.nextInt(Nomi.size()));
				figlio.cognome = Cognomi.get(random.nextInt(Cognomi.size()));
				figlio.vocabolario.clear();
				figlio.dizionario_out.clear();
				figlio.interazioni.clear();
				figlio.conoscenti.clear();
				figlio.ListaCarattereDominante.clear();
				figlio.CompilaListe();
			}

		}
		figlio.vocabolario.putAll(vocabolario);
		figlio.dizionario_out.putAll(dizionario_out);
		appenaNati.add(figlio);
		figli++;
	}

	public void ScambiaMessaggio(Giocatore conoscente) throws InterruptedException {
		//se oggi non si sono ancora scambiati messaggi allora il destinatario riceve dal mittente e poi il mittente riceve dal destinatario
		if (interazioni.get(conoscente).data_ultimo_mex_ricevuto != giorno) {
			if (slow) {
				System.out.println(nome +  " " + cognome + " (umore: " + Round(umore) + " carattere: " + carattere.carattere + ") incontra " + conoscente.nome + " " + conoscente.cognome + " (umore: " + Round(conoscente.umore) + " carattere: " + conoscente.carattere.carattere + ")");
				Thread.sleep(sleep);
			}
			conoscente.Assimila_messaggio(Produci_messaggio(conoscente), this);
			if (conoscente.AggiornaGiocatore()) Assimila_messaggio(conoscente.Produci_messaggio(this), conoscente);
			if (slow) {
				System.out.println(nome +  " " + cognome + " (umore: " + Round(umore) + " carattere: " + carattere.carattere + ") " + conoscente.nome + " " + conoscente.cognome + " (umore: " + Round(conoscente.umore) + " carattere: " + conoscente.carattere.carattere + ")");
				Thread.sleep(sleep);
			}
		}
	}

	private int Produci_messaggio(Giocatore destinatario) throws InterruptedException {
		int messaggio = carattere.output(destinatario, this);
		if (slow) {
			System.out.println(nome + " " + cognome + " invia " + messaggio + " (per lui significa " + vocabolario.get(messaggio) + ")");
			Thread.sleep(sleep);
		}
		return messaggio;
	}

	private void Assimila_messaggio(int messaggio, Giocatore mittente) throws InterruptedException {
		interazioni.get(mittente).data_ultimo_mex_ricevuto = giorno;
		interazioni.get(mittente).storico.addFirst(carattere.input(messaggio, mittente, this));
		if (slow) {
			System.out.println(nome + " " + cognome + " assimila " + messaggio + " (per lui significa " + vocabolario.get(messaggio) + ") come " + interazioni.get(mittente).storico.get(0));
			Thread.sleep(sleep);
		}
		umore += interazioni.get(mittente).storico.get(0) * interazioni.get(mittente).importanza / DivisoreIncrementoUmore;
	}

	public void Crea_Amicizia(Giocatore giocatore) throws InterruptedException {
		int a = random.nextInt(11)-5;
		int b = random.nextInt(11)-5;
		if (a >= 0 && b >= 0) {
			if (interazioni.containsKey(giocatore) == false) {
				if (giocatore.carattere.carattere.equals("player")) System.out.println("\nHai fatto amicizia con " + this.nome + " " + this.cognome + ", e' un tipo " + this.carattere.carattere);
				if (this.carattere.carattere.equals("player")) System.out.println("\nHai fatto amicizia con " + giocatore.nome + " " + giocatore.cognome + ", e' un tipo " + giocatore.carattere.carattere);
				conoscenti.add(giocatore);
				interazioni.put(giocatore, new ValoreInterazione());
				giocatore.conoscenti.add(this);
				giocatore.interazioni.put(this, new ValoreInterazione());
				if (slow) {
					System.out.println(giocatore.nome + " " + giocatore.cognome + " ha stretto amicizia con " + this.nome + " " + this.cognome);
					Thread.sleep(sleep);			
				}
			}
		}
	}

	private void CompilaListe() {
		int parola;
		ListaCarattereDominante.addAll(caratteri);
		for (int i=0; i<caratteri.size()/4; i++) ListaCarattereDominante.add(carattere.carattere);
		for (int i=-5; i<6; i++) {
			dizionario_out.put(i, new LinkedList<Integer>());
			while(true) {
				parola = random.nextInt(101);
				if (vocabolario.containsKey(parola)) continue;
				else vocabolario.put(parola, i); break;
			}
			dizionario_out.get(i).add(parola);
		}
	}

	public boolean ControllaAmicizia(Giocatore conoscente) throws InterruptedException {
		if (interazioni.get(conoscente).importanza<1) {
			amicizieRotte.add(new Tupla(this, conoscente));
			if (slow) {
				System.out.println(nome + " " + cognome + " rompe l'amicizia con " + conoscente.nome + " " + conoscente.cognome);
				Thread.sleep(sleep);
			}
			return false;
		}
		return true;
	}
}