package com.baozi.baozistore.controller;

import com.baozi.baozistore.model.Pedido;
import com.baozi.baozistore.repository.ClienteRepository;
import com.baozi.baozistore.repository.PedidoRepository;
import com.baozi.baozistore.repository.ProdutoRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            ClienteRepository clienteRepository,
                            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
        if (pedido.getClienteId() == null || !clienteRepository.existsById(pedido.getClienteId())) {
            return ResponseEntity.badRequest().body("Cliente nao encontrado");
        }
        if (pedido.getProdutoId() == null || !produtoRepository.existsById(pedido.getProdutoId())) {
            return ResponseEntity.badRequest().body("Produto nao encontrado");
        }
        if (pedido.getQuantidade() == null || pedido.getQuantidade() <= 0) {
            return ResponseEntity.badRequest().body("Quantidade deve ser maior que zero");
        }
        pedido.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoRepository.save(pedido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> apagar(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(p -> {
                    pedidoRepository.delete(p);
                    return ResponseEntity.ok(p);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}