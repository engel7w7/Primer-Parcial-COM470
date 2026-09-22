package test.springboot.app.repositories;

import test.springboot.app.Datos;
import test.springboot.app.models.Banco;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class BancoRepositoryImpl implements BancoRepository {

    private final Map<Long, Banco> bancos = new ConcurrentHashMap<>();

    public BancoRepositoryImpl() {
        Banco b = Datos.crearBanco();
        bancos.put(b.getId(), b);
    }

    @Override
    public List<Banco> findAll() {
        return bancos.values().stream().collect(Collectors.toList());
    }

    @Override
    public Banco findById(Long id) {
        return bancos.get(id);
    }

    @Override
    public void update(Banco banco) {
        bancos.put(banco.getId(), banco);
    }
}