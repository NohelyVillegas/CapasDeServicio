package com.espe.micro_cursos.services;

import com.espe.micro_cursos.clients.UsuarioClientRest;
import com.espe.micro_cursos.models.Usuario;
import com.espe.micro_cursos.models.entities.Curso;
import com.espe.micro_cursos.models.entities.CursoUsuario;
import com.espe.micro_cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService{
    @Autowired
    private CursoRepository repository;

    @Autowired
    private UsuarioClientRest clientRest;


    @Override
    public List<Curso> findAll() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    public Optional<Curso> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Curso save(Curso curso) {
        return  repository.save(curso);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> addUsuario(Usuario usuario, Long id) {
        Optional<Curso> optional= repository.findById(id);
        if (optional.isPresent()){
          Usuario usuarioTemp= clientRest.findById(usuario.getId());

          Curso curso= optional.get();
            CursoUsuario cursoUsuario= new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioTemp.getId());
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioTemp);
        }
        return Optional.empty();
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        return clientRest.save(usuario);
    }

    @Override
    public Optional<Usuario> removeUsuario(Long usuarioId, Long cursoId) {
        Optional<Curso> cursoOptional = repository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            curso.removeCursoUsuarioByUsuarioId(usuarioId);
            repository.save(curso);
            return Optional.of(clientRest.findById(usuarioId));
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> getUsuariosByCursoId(Long cursoId) {
        Optional<Curso> cursoOptional = repository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            List<Usuario> usuarios = new ArrayList<>();
            for (CursoUsuario cu : curso.getCursoUsuarios()) {
                Usuario usuario = clientRest.findById(cu.getUsuarioId());
                usuarios.add(usuario);
            }
            return usuarios;
        }
        return Collections.emptyList();
    }

}
