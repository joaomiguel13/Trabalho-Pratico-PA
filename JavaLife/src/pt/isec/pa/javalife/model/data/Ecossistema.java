package pt.isec.pa.javalife.model.data;

import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngineEvolve;
import pt.isec.pa.javalife.model.memento.MyOriginator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Ecossistema implements Serializable, IGameEngineEvolve {
    private Set<IElemento> elementos;
    private final int altura;// ACHO QUE ESTA MERDA NEM EXISTE
    private final int largura;
    private final int tempo;
    PropertyChangeSupport pcs;

    boolean applySol = false;
    private int tempoEventoSol;

    private boolean saved;

    public Ecossistema(int altura, int largura, int tempo) {
        this.altura = altura;
        this.largura = largura;
        this.tempo = tempo;
        this.tempoEventoSol = 0;

        elementos = new HashSet<>();

        int nEsquerda = altura / 75;
        int nDireita = altura / 75;
        int nCima = largura / 25;
        int nBaixo = largura / 25;

        //PARTE ESQUERDA
        for (int i = 0; i < nEsquerda; i++) {
            elementos.add(new Inanimado(new Area(75 * i, 0, 75 * (i + 1), 25)));
        }

        //PARTE DE CIMA
        for (int i = 0; i < nCima; i++) {
            elementos.add(new Inanimado(new Area(0, 75 * i, 25, 75 * (i + 1))));
        }

        //PARTE DIREITA
        for (int i = 0; i < nDireita; i++) {
            elementos.add(new Inanimado(new Area(75 * i, largura-40, 75 * (i + 1), largura)));
        }

        //PARTE DE BAIXO
        for (int i = 0; i < nBaixo; i++) {
            elementos.add(new Inanimado(new Area(altura-50, 75 * i, altura-75, 75 * (i + 1))));
        }



        pcs = new PropertyChangeSupport(this);
    }

    public Set<IElemento> getElementos() {
        return elementos;
    }

    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {
        System.out.println("Evolve");
        saved = false;

        Set<IElemento> elementosCopia = new HashSet<>(elementos);;

        if(applySol){
            for(IElemento elemento : elementos){
                if(elemento.getType() == Elemento.FAUNA) {
                    if(((Fauna) elemento).getDefaultSpeed() != ((Fauna) elemento).getSolValue()){
                        ((Fauna) elemento).setDefaultSpeed(((Fauna) elemento).getDefaultSpeed() / 2);
                    }
                }
                else if(elemento.getType() == Elemento.FLORA){
                    if(((Flora) elemento).getDefaultIncrement() != ((Flora) elemento).getSolValue()){
                        ((Flora) elemento).setDefaultIncrement(((Flora) elemento).getDefaultIncrement()*2);
                    }
                }
            }
            tempoEventoSol--;
            System.out.println("TempoEventoSol:" + tempoEventoSol);
            if(tempoEventoSol <= 0){
                applySol = false;
                for(IElemento elemento : elementos) {
                    if (elemento.getType() == Elemento.FAUNA) {
                        ((Fauna) elemento).setDefaultSpeed(((Fauna) elemento).getDefaultSpeed() * 2);
                    } else if (elemento.getType() == Elemento.FLORA) {
                        ((Flora) elemento).setDefaultIncrement(((Flora) elemento).getDefaultIncrement() / 2);
                    }
                }
            }
        }

        for (IElemento e : elementosCopia) {
            e.evolve();
            if(e.getType()== Elemento.FAUNA){
                System.out.println("Forca:" + ((Fauna) e).getForca());
                System.out.println("Estado:"+((Fauna) e).getState() + " ID:" + ((Fauna) e).getId());
                System.out.println("Velocidade:" + ((Fauna) e).getDefaultSpeed());
            }
            if (e.getType()== Elemento.FLORA) {
                System.out.println("Forca:" + ((Flora) e).getForca());
                if (((Flora) e).getForca() >= 90) {
                    Area areaFilho;
                    Area area = e.getArea();
                    boolean direita = true, esquerda = true, cima = true, baixo = true;
                    areaFilho = new Area(area.cima(), area.esquerda() + (area.direita() - area.esquerda()), area.baixo(), area.direita() + (area.direita() - area.esquerda()));
                    for (IElemento eFlora : elementos) {
                        if (eFlora.getArea().intersects(areaFilho) ) {
                            direita = false;
                            break;
                        }
                    }


                    //ESQUERDA
                    areaFilho = new Area(area.cima(), area.esquerda() - (area.direita() - area.esquerda()), area.baixo(), area.direita() - (area.direita() - area.esquerda()));
                    for (IElemento eFlora : elementos) {
                        if (eFlora.getArea().intersects(areaFilho)) {
                            esquerda = false;
                            break;
                        }
                    }
                    //CIMA
                    areaFilho = new Area(area.cima() - (area.baixo() - area.cima()), area.esquerda(), area.baixo() - (area.baixo() - area.cima()), area.direita());
                    for (IElemento eFlora : elementos) {
                        if (eFlora.getArea().intersects(areaFilho)) {
                            cima = false;
                            break;
                        }
                    }
                    //BAIXO

                    areaFilho = new Area(area.cima() + (area.baixo() - area.cima()), area.esquerda(), area.baixo() + (area.baixo() - area.cima()), area.direita());
                    for (IElemento eFlora : elementos) {
                        if (eFlora.getArea().intersects(areaFilho)) {
                            baixo = false;
                            break;
                        }
                    }
                    boolean done = false;
                    Random random = new Random();
                    if(direita || esquerda || cima || baixo){
                        if(((Flora) e).getContadorReproducao() >= 2){
                            break;
                        }
                        while(!done){
                            int n = random.nextInt(4);
                            switch (n) {
                                case 0 -> {
                                    if (direita ) {
                                        Flora flora = new Flora(new Area(area.cima(), area.esquerda() + (area.direita() - area.esquerda()), area.baixo(), area.direita() + (area.direita() - area.esquerda())),50,((Flora) e).getPerdaForca());
                                        elementos.add(flora);
                                        ((Flora) e).setContadorReproducao(((Flora) e).getContadorReproducao() + 1);
                                        ((Flora) e).setForca(60);
                                        flora.setImagem("rectangle.png");
                                        done = true;
                                    }
                                }
                                case 1 -> {
                                    if (esquerda) {
                                        Flora flora = new Flora(new Area(area.cima(), area.esquerda() - (area.direita() - area.esquerda()), area.baixo(), area.direita() - (area.direita() - area.esquerda())),50,((Flora) e).getPerdaForca());
                                        elementos.add(flora);
                                        ((Flora) e).setContadorReproducao(((Flora) e).getContadorReproducao() + 1);
                                        ((Flora) e).setForca(60);
                                        flora.setImagem("rectangle.png");
                                        done = true;
                                    }
                                }
                                case 2 -> {
                                    if (cima) {
                                        Flora flora = new Flora(new Area(area.cima() - (area.baixo() - area.cima()), area.esquerda(), area.baixo() - (area.baixo() - area.cima()), area.direita()),50,((Flora) e).getPerdaForca());
                                        elementos.add(flora);
                                        ((Flora) e).setContadorReproducao(((Flora) e).getContadorReproducao() + 1);
                                        ((Flora) e).setForca(60);
                                        flora.setImagem("rectangle.png");
                                        done = true;
                                    }
                                }
                                case 3 -> {
                                    if (baixo) {
                                        Flora flora = new Flora(new Area(area.cima() + (area.baixo() - area.cima()), area.esquerda(), area.baixo() + (area.baixo() - area.cima()), area.direita()),50,((Flora) e).getPerdaForca());
                                        elementos.add(flora);
                                        ((Flora) e).setContadorReproducao(((Flora) e).getContadorReproducao() + 1);
                                        ((Flora) e).setForca(60);
                                        flora.setImagem("rectangle.png");
                                        done = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (IElemento e : elementosCopia) {
            if (e.getType()== Elemento.FAUNA) {
                ((Fauna) e).atualizaMapa(elementos);
            }
            if(e.getType()== Elemento.FLORA){
                ((Flora) e).atualizaMapa(elementos);
            }
        };

        pcs.firePropertyChange("ola", null, null);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public void removeFauna(int lastID) {
        for(IElemento e : elementos){
            if(e.getId() == lastID && e.getType()== Elemento.FAUNA){
                elementos.remove(e);
                break;
            }
        }
    }

    public void addFaunaRedo(IElemento temp) {
        elementos.add(temp);
    }

    public void addFloraRedo(IElemento temp) {
        elementos.add(temp);
    }

    public void removeFloraCommand(int lastID) {
        for(IElemento e : elementos){
            if(e.getId() == lastID && e.getType()== Elemento.FLORA){
                elementos.remove(e);
                break;
            }
        }
    }

    public void removeInanimadoCommand(int lastID) {
        for(IElemento e : elementos) {
            if (e.getId() == lastID && e.getType()== Elemento.INANIMADO) {
                elementos.remove(e);
                break;
            }
        }
    }

    public void applySolCommand(){
        applySol = true;
        tempoEventoSol = 10;
    }

    public boolean isApplySol() {
        return applySol;
    }

    public void addInanimadoRedo(IElemento temp) {
        elementos.add(temp);
    }


    public void replaceElementos(Set<IElemento> elementos){
        this.elementos = elementos;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }


    public void addFlora_command(double forcaDefault, double perdaForca,int x,int y, int x1){
        Flora flora = new Flora(new Area(x, y, x + x1+5, y + x1), forcaDefault, perdaForca);
        flora.setImagem("rectangle.png");
        elementos.add(flora);
    }

    public void addFauna_command(double defaultForca, double perdaForcaMovimento, double ganhaForcaAlimento,int x,int y){
        Fauna fauna = new Fauna(new Area(x, y, x+25, y+25), elementos, defaultForca, perdaForcaMovimento, ganhaForcaAlimento);
        elementos.add(fauna);
    }

}
