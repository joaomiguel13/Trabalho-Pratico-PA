package pt.isec.pa.javalife.model.fsm.states;

import pt.isec.pa.javalife.model.data.*;
import pt.isec.pa.javalife.model.fsm.JavaLifeState;
import pt.isec.pa.javalife.model.fsm.JavaLifeStateAdapter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ReproducingState extends JavaLifeStateAdapter {
    public ReproducingState(Fauna data) {
        super(data);
    }

    public JavaLifeState getState() {
        return JavaLifeState.REPRODUCING;
    }

    @Override
    public void evolve() {
        IElemento faunaProxima = null;

        double distancia;
        boolean done = false;
        Set<IElemento> copyOfElementos = new HashSet<>(data.getElementos());
        boolean colide = false;
        while (!done) {
            double maiorForca = Double.MIN_VALUE;
            for (IElemento e : copyOfElementos) {
                if (e instanceof Fauna && e.getId() != data.getId()) {
                    if (((Fauna) e).getForca() > maiorForca) {
                        maiorForca = ((Fauna) e).getForca();
                        faunaProxima = e;
                    }
                }
            }
            if (faunaProxima != null) {
                Area areaFauna = faunaProxima.getArea();
                Area minhaArea = data.getArea();
                //verificar se o meu target esta adjacente
                distancia = Area.calcularMenorDistancia(minhaArea, areaFauna);


                double centroXMinha = (minhaArea.esquerda() + minhaArea.direita()) / 2;
                double centroYMinha = (minhaArea.cima() + minhaArea.baixo()) / 2;
                double centroXFlora = (areaFauna.esquerda() + areaFauna.direita()) / 2;
                double centroYFlora = (areaFauna.cima() + areaFauna.baixo()) / 2;

                int direction = -1;
                if (Math.abs(centroXFlora - centroXMinha) > Math.abs(centroYFlora - centroYMinha)) {
                    if (centroXFlora > centroXMinha) {
                        direction = 0; // Direita
                    } else {
                        direction = 3; // Esquerda
                    }
                } else {
                    if (centroYFlora > centroYMinha) {
                        direction = 2; // Baixo
                    } else {
                        direction = 1; // Cima
                    }
                }

                switch (direction) {
                    case 0 -> { // Direita
                        for (IElemento e : copyOfElementos) {
                            if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {
                                colide = true;
                                break;
                            }
                        }
                        if (!colide) {
                            if (data.getArea().direita() + 5 < 800 - 25) {
                                data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5));
                                data.diminiuForca();
                                done = true;
                            }
                        }
                        else {
                            done = true;
                        }
                    }
                    case 1 -> { // Cima
                        for (IElemento e : copyOfElementos) {
                            if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {

                                colide = true;
                                break;
                            }
                        }
                        if (!colide) {
                            if (data.getArea().cima() - 5 > 25) {
                                data.setArea(new Area(data.getArea().cima() - 5, data.getArea().esquerda(), data.getArea().baixo() - 5, data.getArea().direita()));
                                data.diminiuForca();
                                done = true;
                            }
                        }
                        else{
                            done = true;
                        }
                    }
                    case 2 -> { // Baixo
                        for (IElemento e : copyOfElementos) {
                            if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {

                                colide = true;
                                break;
                            }
                        }
                        if (!colide) {
                            if (data.getArea().baixo() + 5 < 550) {
                                data.setArea(new Area(data.getArea().cima() + 5, data.getArea().esquerda(), data.getArea().baixo() + 5, data.getArea().direita()));
                                data.diminiuForca();
                                done = true;
                            }
                        }
                        else{
                            done = true;
                        }

                    }
                    case 3 -> { // Esquerda
                        for (IElemento e : copyOfElementos) {
                            if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {

                                colide = true;
                                break;
                            }
                        }
                        if (!colide) {
                            if (data.getArea().esquerda() - 5 > 25) {
                                data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() - 5, data.getArea().baixo(), data.getArea().direita() - 5));
                                data.diminiuForca();
                                done = true;
                            }
                        }
                        else{
                            done = true;
                        }
                    }
                }
                if (distancia > 5) {
                    data.changeState(new MovingState(data));
                    done = true;
                } else {
                    if (data.getForca() > 25) {
                        data.setSecondsToReproduce(data.getSecondsToReproduce() + 1);
                        System.out.println("segundos: " + data.getSecondsToReproduce());
                        if (data.getSecondsToReproduce() == 10) {
                            System.out.println("Reproduzir");
                            Area areaFilho;
                            Area area = data.getArea();
                            boolean direita = true, esquerda = true, cima = true, baixo = true;
                            areaFilho = new Area(area.cima(), area.esquerda() + (area.direita() - area.esquerda()) + 10, area.baixo(), area.direita() + (area.direita() - area.esquerda()) + 10);
                            for (IElemento f : data.getElementos()) {
                                if (f.getArea().intersects(areaFilho) && f.getType() == Elemento.INANIMADO) {
                                    direita = false;
                                    break;
                                }
                            }
                            //ESQUERDA
                            areaFilho = new Area(area.cima(), area.esquerda() - (area.direita() - area.esquerda() - 10), area.baixo(), area.direita() - (area.direita() - area.esquerda()) - 10);
                            for (IElemento f : data.getElementos()) {
                                if (f.getArea().intersects(areaFilho) && f.getType() == Elemento.INANIMADO) {
                                    esquerda = false;
                                    break;
                                }
                            }
                            //CIMA
                            areaFilho = new Area(area.cima() - (area.baixo() - area.cima()) - 10, area.esquerda(), area.baixo() - (area.baixo() - area.cima()) - 10, area.direita());
                            for (IElemento f : data.getElementos()) {
                                if (f.getArea().intersects(areaFilho) && f.getType() == Elemento.INANIMADO) {
                                    cima = false;
                                    break;
                                }
                            }
                            //BAIXO
                            areaFilho = new Area(area.cima() + (area.baixo() - area.cima()) + 10, area.esquerda(), area.baixo() + (area.baixo() - area.cima()) + 10, area.direita());
                            for (IElemento f : data.getElementos()) {
                                if (f.getArea().intersects(areaFilho) && f.getType() == Elemento.INANIMADO) {
                                    baixo = false;
                                    break;
                                }
                            }
                            boolean donee = false;
                            Random random = new Random();
                            if (direita || esquerda || cima || baixo) {
                                while (!donee) {
                                    int n = random.nextInt(4);
                                    switch (n) {
                                        case 0 -> {
                                            if (direita) {
                                                Fauna fauna = new Fauna((new Area(area.cima(), area.esquerda() + (area.direita() - area.esquerda()) + 10, area.baixo(), area.direita() + (area.direita() - area.esquerda()) + 10)), data.getElementos(), 50, data.getPerdaForcaMovimento(), data.getGanhaForcaAlimento());
                                                data.getElementos().add(fauna);
                                                fauna.setImagem("cabeca-de-leao.png");
                                                donee = true;
                                            }
                                        }
                                        case 1 -> {
                                            if (esquerda) {
                                                Fauna fauna = new Fauna((new Area(area.cima(), area.esquerda() - (area.direita() - area.esquerda()) - 10, area.baixo(), area.direita() - (area.direita() - area.esquerda()) - 10)), data.getElementos(), 50, data.getPerdaForcaMovimento(), data.getGanhaForcaAlimento());
                                                data.getElementos().add(fauna);
                                                fauna.setImagem("cabeca-de-leao.png");
                                                donee = true;
                                            }
                                        }
                                        case 2 -> {
                                            if (cima) {
                                                Fauna fauna = new Fauna((new Area(area.cima() - (area.baixo() - area.cima()) - 10, area.esquerda(), area.baixo() - (area.baixo() - area.cima()) - 10, area.direita())), data.getElementos(), 50, data.getPerdaForcaMovimento(), data.getGanhaForcaAlimento());
                                                data.getElementos().add(fauna);
                                                fauna.setImagem("cabeca-de-leao.png");
                                                donee = true;
                                            }
                                        }
                                        case 3 -> {
                                            if (baixo) {
                                                Fauna fauna = new Fauna((new Area(area.cima() + (area.baixo() - area.cima()) + 10, area.esquerda(), area.baixo() + (area.baixo() - area.cima()) + 10, area.direita())), data.getElementos(), 50, data.getPerdaForcaMovimento(), data.getGanhaForcaAlimento());
                                                data.getElementos().add(fauna);
                                                fauna.setImagem("cabeca-de-leao.png");
                                                donee = true;
                                            }
                                        }
                                    }
                                }
                            }
                            data.setForca(data.getForca() - 25);
                            data.setSecondsToReproduce(0);
                            if(data.getForca() < 35){
                                data.changeState(new SearchingFoodState(data));
                                done = true;
                            }else{
                                data.changeState(new MovingState(data));
                                done = true;
                            }
                        }
                    }else {
                        data.changeState(new MovingState(data));
                        done = true;
                    }
                }
            }
        }
    }
}
