package pt.isec.pa.javalife.model.fsm.states;

import pt.isec.pa.javalife.model.data.*;
import pt.isec.pa.javalife.model.fsm.JavaLifeState;
import pt.isec.pa.javalife.model.fsm.JavaLifeStateAdapter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MovingState extends JavaLifeStateAdapter {
    public MovingState(Fauna data) {
        super(data);
    }

    @Override
    public JavaLifeState getState() {
        return JavaLifeState.MOVING;
    }

    @Override
    public void evolve() {
        boolean done = false;
        boolean colide = false;
        Set<IElemento> copyOfElementos = new HashSet<>(data.getElementos());
        while (!done) {
            if (data.getForca() > 50) {
                IElemento faunaProxima = null;

                double distancia;
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

                                        data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5));
                                        data.diminiuForca();
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

                                        data.setArea(new Area(data.getArea().cima() - 5, data.getArea().esquerda(), data.getArea().baixo() - 5, data.getArea().direita()));
                                        data.diminiuForca();
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

                                        data.setArea(new Area(data.getArea().cima() + 5, data.getArea().esquerda(), data.getArea().baixo() + 5, data.getArea().direita()));
                                        data.diminiuForca();
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

                                        data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() - 5, data.getArea().baixo(), data.getArea().direita() - 5));
                                        data.diminiuForca();
                                        done = true;


                                }
                            }
                        }
                        if (distancia <= 5){
                            data.changeState(new ReproducingState(data));
                            done = true;
                        }

                        if (data.getForca() <= 0) {
                            data.changeState(new DeadState(data));
                        }
                    }else{
                        colide = false;
                        Random random = new Random();
                        int direction = random.nextInt(4);
                        switch (direction) {
                            case 0 -> { // direita

                                for (IElemento e : copyOfElementos) {
                                    if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + data.getDefaultSpeed(), data.getArea().baixo(), data.getArea().direita() + data.getDefaultSpeed())) && e.getId() != data.getId() &&  e.getType()== Elemento.INANIMADO) {
                                        colide = true;
                                        break;
                                    }
                                }
                                if (!colide) {
                                    data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() + data.getDefaultSpeed(), data.getArea().baixo(), data.getArea().direita() + data.getDefaultSpeed()));
                                    data.diminiuForca();
                                    if (data.getForca() < 35) {
                                        data.changeState(new SearchingFoodState(data));
                                    }
                                    done = true;
                                }

                            }
                            case 1 -> { //cima

                                for (IElemento e : copyOfElementos) {
                                    if (e.getArea().intersects(new Area(data.getArea().cima() - data.getDefaultSpeed(), data.getArea().esquerda(), data.getArea().baixo() - data.getDefaultSpeed(), data.getArea().direita())) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {
                                        colide = true;
                                        break;
                                    }
                                }
                                if (!colide) {
                                    data.setArea(new Area(data.getArea().cima() - data.getDefaultSpeed(), data.getArea().esquerda(), data.getArea().baixo() - data.getDefaultSpeed(), data.getArea().direita()));
                                    data.diminiuForca();
                                    if (data.getForca() < 35) {
                                        data.changeState(new SearchingFoodState(data));
                                    }
                                    done = true;
                                }

                            }
                            case 2 -> { //Baixo

                                for (IElemento e : copyOfElementos) {
                                    if (e.getArea().intersects(new Area(data.getArea().cima() + data.getDefaultSpeed(), data.getArea().esquerda(), data.getArea().baixo() + data.getDefaultSpeed(), data.getArea().direita())) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {
                                        colide = true;
                                        break;
                                    }
                                }
                                if (!colide) {
                                    data.setArea(new Area(data.getArea().cima() + data.getDefaultSpeed(), data.getArea().esquerda(), data.getArea().baixo() + data.getDefaultSpeed(), data.getArea().direita()));
                                    data.diminiuForca();
                                    if (data.getForca() < 35) {
                                        data.changeState(new SearchingFoodState(data));
                                    }
                                    done = true;
                                }


                            }
                            case 3 -> { //esquerda

                                for (IElemento e : copyOfElementos) {
                                    if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() - data.getDefaultSpeed(), data.getArea().baixo(), data.getArea().direita() - data.getDefaultSpeed())) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {
                                        colide = true;
                                        break;
                                    }
                                }

                                if (!colide) {
                                    data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() - data.getDefaultSpeed(), data.getArea().baixo(), data.getArea().direita() - data.getDefaultSpeed()));
                                    data.diminiuForca();
                                    if (data.getForca() < 35) {
                                        data.changeState(new SearchingFoodState(data));
                                    }
                                    done = true;
                                }
                            }
                        }
                    }
                }
            } else {
                colide = false;
                Random random = new Random();
                int direction = random.nextInt(4);
                switch (direction) {
                    case 0 -> { // direita

                            for (IElemento e : copyOfElementos) {
                                if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + data.getDefaultSpeed(), data.getArea().baixo(), data.getArea().direita() + data.getDefaultSpeed())) && e.getId() != data.getId() &&  e.getType()== Elemento.INANIMADO) {
                                    colide = true;
                                    break;
                                }
                            }
                            if (!colide) {
                                data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() + data.getDefaultSpeed(), data.getArea().baixo(), data.getArea().direita() + data.getDefaultSpeed()));
                                data.diminiuForca();
                                if (data.getForca() < 35) {
                                    data.changeState(new SearchingFoodState(data));
                                }
                                done = true;
                            }

                    }
                    case 1 -> { //cima

                            for (IElemento e : copyOfElementos) {
                                if (e.getArea().intersects(new Area(data.getArea().cima() - data.getDefaultSpeed(), data.getArea().esquerda(), data.getArea().baixo() - data.getDefaultSpeed(), data.getArea().direita())) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {
                                    colide = true;
                                    break;
                                }
                            }
                            if (!colide) {
                                data.setArea(new Area(data.getArea().cima() - data.getDefaultSpeed(), data.getArea().esquerda(), data.getArea().baixo() - data.getDefaultSpeed(), data.getArea().direita()));
                                data.diminiuForca();
                                if (data.getForca() < 35) {
                                    data.changeState(new SearchingFoodState(data));
                                }
                                done = true;
                            }

                    }
                    case 2 -> { //Baixo

                            for (IElemento e : copyOfElementos) {
                                if (e.getArea().intersects(new Area(data.getArea().cima() + data.getDefaultSpeed(), data.getArea().esquerda(), data.getArea().baixo() + data.getDefaultSpeed(), data.getArea().direita())) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {
                                    colide = true;
                                    break;
                                }
                            }
                            if (!colide) {
                                data.setArea(new Area(data.getArea().cima() + data.getDefaultSpeed(), data.getArea().esquerda(), data.getArea().baixo() + data.getDefaultSpeed(), data.getArea().direita()));
                                data.diminiuForca();
                                if (data.getForca() < 35) {
                                    data.changeState(new SearchingFoodState(data));
                                }
                                done = true;
                            }


                    }
                    case 3 -> { //esquerda

                            for (IElemento e : copyOfElementos) {
                                if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() - data.getDefaultSpeed(), data.getArea().baixo(), data.getArea().direita() - data.getDefaultSpeed())) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {
                                    colide = true;
                                    break;
                                }
                            }

                        if (!colide) {
                            data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() - data.getDefaultSpeed(), data.getArea().baixo(), data.getArea().direita() - data.getDefaultSpeed()));
                            data.diminiuForca();
                            if (data.getForca() < 35) {
                                data.changeState(new SearchingFoodState(data));
                            }
                            done = true;
                        }
                    }
                }
            }
        }
    }
}
