import java.util.*;
//import java.awt.*;
//import javax.swing.*;
//import javafx.*;
import java.io.*;
//progetto backup 02.06.19
public class Center {
	static int sleep = 0;
	static boolean slow = true;
	static boolean player = false;   //||||||||||||| True = Modalita' Gioca ||||||||||||| False = Modalita' Guarda |||||||||||||
	static boolean isalive;
	static boolean p;
	static boolean continua = true;
	static int ContatoreMorti = 0;
	static int LimiteFigli;
	static int DivisoreIncrementoUmore;
	static int EtaMassima;
	static int size;
	static int NatiOggi;
	static int MortiOggi;
	static int giorno = 1;
	static int c;
	static double ImportanzaIniziale;
	static double UmoreIniziale;
	static Giocatore io;
	static HashMap<String, Integer> contCaratteri = new HashMap<String, Integer>();
	static HashSet<Giocatore> MortiViventi = new HashSet<Giocatore>();
	static HashSet<Giocatore> appenaNati = new HashSet<Giocatore>();
	static HashSet<Tupla> amicizieRotte = new HashSet<Tupla>();
	static LinkedList<Giocatore> ins = new LinkedList<Giocatore>();
	static LinkedList<Giocatore> cimitero = new LinkedList<Giocatore>();
	static LinkedList<String> l = new LinkedList<String>();
	static LinkedList<String> Nomi = new LinkedList<String>();
	static LinkedList<String> Cognomi = new LinkedList<String>();
	static LinkedList<String> caratteri = new LinkedList<String>();
	static Random random = new Random();
	static Scanner inputScanner = new Scanner(System.in);


	public static void main(String[] args) throws IOException, InterruptedException {
		Start();
		while(c > 0 && ins.size() > 1) {
			if (player) StampaInizioPlayer();
			if (slow) {
				System.out.println("\nGiorno "+ giorno + " Nel mondo ci sono " + ins.size() + " persone");
				Thread.sleep(sleep);
			}
			NuoveAmicizie();
			VitaSociale();
			aggiornaInsCimitero();

			if (player) {
				StampaFinePlayer();
				if (!isalive) break;
			}
			else Stampa();

			giorno++;
			c--;
			//parametri di controllo popolazione
			if (ins.size()>10000) { //sovrapopolazione
				DivisoreIncrementoUmore = 4;
				EtaMassima = 30;
				ImportanzaIniziale = 2;
			}
			else if (ins.size()<100) { //carestia
				DivisoreIncrementoUmore = 2;
				EtaMassima = 60;
				ImportanzaIniziale = 3;
			}
			else { //modalita normale
				DivisoreIncrementoUmore = 3;
				EtaMassima = 50;
				ImportanzaIniziale = 2.5;
			}
		}
	}

	
	static double Round(double numero) { //metodo per arrotondare
		numero *= 100;
		float parteintera = (float)numero;
		int uscita = Math.round(parteintera);
		return (double)uscita/100;
	}
	
	static void Start() throws IOException {
		LimiteFigli = 3;
		DivisoreIncrementoUmore = 3;
		EtaMassima = 50;
		ImportanzaIniziale = 2.5;
		UmoreIniziale = 50;
		continua=true;
		do {
			try {
				System.out.print("Inserisci il numero dei giocatori iniziali: ");
				size = inputScanner.nextInt();
				if (size<2) throw new InputMismatchException();
				continua=false;
			} catch (InputMismatchException e) {
				System.out.print("Errore! ");
			}
			inputScanner.nextLine(); 
		} while (continua);

		if (player == true) { //modalita gioca
			p=true;
			isalive=true;
			size--;
			l.add("player");
			io=new Giocatore(l);
			ins.add(io);
			c = io.giorno_morte; 
		}
		else {
			continua=true;
			do {
				try {
					System.out.print("Inserisci il numero dei giorni: ");
					c = inputScanner.nextInt();
					if (c<2) throw new InputMismatchException();
					continua=false;
				} catch (InputMismatchException e) {
					System.out.print("Errore! ");
				}
				inputScanner.nextLine(); 
			} while (continua);
		}
		if (size<2) {
			System.out.println("Inserire almeno due giocatori");
			Start();
		}
		Genera_liste();
		for (int i=0; i<size; i++) {
			ins.add(new Giocatore(caratteri));
			contCaratteri.replace(ins.getLast().carattere.carattere, contCaratteri.get(ins.getLast().carattere.carattere)+1);
		}
	}

	static void Genera_liste() throws IOException {
		String fileName = "src\\listanomi.txt";
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line;
		while((line = br.readLine()) != null){
			Nomi.add(line);
		}
		br.close();
		String fileName1 = "src\\listacognomi.txt";
		File file1 = new File(fileName1);
		FileInputStream fis1 = new FileInputStream(file1);
		InputStreamReader isr1 = new InputStreamReader(fis1,"UTF-8");
		BufferedReader br1 = new BufferedReader(isr1);

		String line1;
		while((line1 = br1.readLine()) != null){
			Cognomi.add(line1);
		}
		br1.close();
		caratteri.add("random");
		caratteri.add("positivo");
		caratteri.add("negativo");
		caratteri.add("bipolare");
		caratteri.add("malvagio");
		caratteri.add("neutrale");
		caratteri.add("rancoroso");
		caratteri.add("visionario");
		for (String carattere: caratteri) contCaratteri.put(carattere, 0);
	}

	static void NuoveAmicizie() throws InterruptedException {
		Collections.shuffle(ins);
		int n = ((ins.size()*60)/100); //possibilita del 60 per cento di fare amicizia
		if (n%2!=0) n+=1;
		for (int i=0; i<n/2; i++) {
			ins.get(i).Crea_Amicizia(ins.get(i+(n/2)));
		}
	}

	static void aggiornaInsCimitero() {
		NatiOggi = appenaNati.size();
		MortiOggi = MortiViventi.size();
		ContatoreMorti += MortiOggi;
		for (Tupla tupla: amicizieRotte) {
			if (tupla.g1.conoscenti.contains(tupla.g2)) { //rimuove un player dalle amicizie
				tupla.g1.conoscenti.remove(tupla.g2);
				tupla.g1.interazioni.remove(tupla.g2);
			}
			if (tupla.g2.conoscenti.contains(tupla.g1)) {
				tupla.g2.conoscenti.remove(tupla.g1);
				tupla.g2.interazioni.remove(tupla.g1);
			}
		}
		for (Giocatore iGiocatore: MortiViventi) {
			ins.remove(iGiocatore);
			cimitero.add(iGiocatore);
			if (!iGiocatore.carattere.carattere.equals("player")) contCaratteri.replace(iGiocatore.carattere.carattere, contCaratteri.get(iGiocatore.carattere.carattere)-1);
			for (Giocatore conoscente: iGiocatore.conoscenti) {
				if (conoscente==io) System.out.println("Il tuo caro amico " + iGiocatore.nome + " " + iGiocatore.cognome + " e' morto! Condoglianze (Il tuo umore scende del 5%)");
				conoscente.umore-=5;
				conoscente.interazioni.remove(iGiocatore);
				conoscente.conoscenti.remove(iGiocatore);
			}
		}
		for (Giocatore iGiocatore: appenaNati) {
			ins.add(iGiocatore);
			contCaratteri.replace(iGiocatore.carattere.carattere, contCaratteri.get(iGiocatore.carattere.carattere)+1);
		}
		MortiViventi.clear();
		appenaNati.clear();
		amicizieRotte.clear();
	}

	static class Tupla {
		Giocatore g1;
		Giocatore g2;
		Tupla(Giocatore g1, Giocatore g2){
			this.g1 = g1;
			this.g2 = g2;
		}
	}

	static void VitaSociale() throws InterruptedException {
		Collections.shuffle(ins);
		for (Giocatore iGiocatore: ins) {
			if (iGiocatore.conoscenti.isEmpty()) { //se la lista dei conoscenti e vuota diminuisce l'umore
				iGiocatore.umore -= 5;
				if (iGiocatore.carattere.carattere.equals("player")) System.out.println("Oggi non hai conosciuto nessuno! Il tuo umore scende del 5%...\n");
				if (slow) {
					System.out.println(iGiocatore.nome + " " + iGiocatore.cognome + " non conosce ancora nessuno e il suo umore scende del 5%");
					Thread.sleep(sleep);
				}
			}
			if (iGiocatore.AggiornaGiocatore() == false) continue;
			Collections.shuffle(iGiocatore.conoscenti);
			for (Giocatore conoscente: iGiocatore.conoscenti) {
				if (MortiViventi.contains(conoscente)) continue; //se il conoscente e' morto salta l'interazione con lui
				iGiocatore.ScambiaMessaggio(conoscente);
				if (iGiocatore.AggiornaGiocatore() == false) break;
				iGiocatore.AggiornaImportanzaConoscente(conoscente);
				if (conoscente.AggiornaGiocatore()) iGiocatore.ControllaAmicizia(conoscente);
				if (iGiocatore.ControllaAmicizia(conoscente)==false && iGiocatore.carattere.carattere.equals("player")) System.out.println("Hai rotto l'amicizia con " + conoscente.nome + " " + conoscente.cognome + ", qualcosa non andava!");
				if (iGiocatore.AggiornaGiocatore() == false) break;
			}
			iGiocatore.giorni_attivi+=1;
			if (MortiViventi.contains(iGiocatore) == false) iGiocatore.AggiornaGiocatore();
		}
	}

	static void Stampa() throws InterruptedException {
		System.out.println("Giorno "+ giorno + " Nati oggi " + NatiOggi + " Morti oggi "+ MortiOggi + " Insieme giocatori "+ ins.size()+" "+"Morti "+ ContatoreMorti);
		System.out.println(contCaratteri.toString());
		if (slow) Thread.sleep(sleep);
	}

	static void StampaInizioPlayer() {
		if (isalive) {
			System.out.println("\nGiorno "+ giorno + " Nel mondo ci sono " + ins.size() + " persone");
			System.out.println("Nome giocatore: " + io.nome + " " + io.cognome);
			if (io.conoscenti.size()==1) System.out.println("Attualmente il tuo umore e' al " + Round(io.umore) + "% e hai " + io.conoscenti.size() + " amico\n");
			else System.out.println("Attualmente il tuo umore e' al " + Round(io.umore) + "% e hai " + io.conoscenti.size() + " amici\n");
		}
	}

	static void StampaFinePlayer() {
		if (isalive) {
			if (io.conoscenti.size()==1) System.out.println("Attualmente il tuo umore e' al " + Round(io.umore) + "% e hai " + io.conoscenti.size() + " amico\n");
			else System.out.println("\nAttualmente il tuo umore e' al " + Round(io.umore) + "% e hai " + io.conoscenti.size() + " amici\n");
			for (Giocatore conoscente: io.conoscenti) {
				System.out.println("Nome amico: " + conoscente.nome + " " + conoscente.cognome + " | Importanza: " + io.interazioni.get(conoscente).importanza + " | Carattere: " + conoscente.carattere.carattere + " | Umore: " + Round(conoscente.umore)); 
			}
			System.out.println();
		}
	}	
}