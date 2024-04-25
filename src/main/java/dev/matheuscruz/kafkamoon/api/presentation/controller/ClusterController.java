package dev.matheuscruz.kafkamoon.api.presentation.controller;

import dev.matheuscruz.kafkamoon.api.usecases.cluster.get.GetClusterInfoUseCase;
import dev.matheuscruz.kafkamoon.api.usecases.cluster.get.GetClusterInfoUseCaseOutput;
import dev.matheuscruz.kafkamoon.api.usecases.cluster.nodes.list.ListNodesUseCase;
import dev.matheuscruz.kafkamoon.api.usecases.cluster.nodes.list.ListNodesUseCaseOutput;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cluster")
public class ClusterController {

  private final GetClusterInfoUseCase getClusterInfoUseCase;
  private final ListNodesUseCase listNodesUseCase;

  public ClusterController(
      final GetClusterInfoUseCase getClusterInfoUseCase, ListNodesUseCase listNodesUseCase) {
    this.getClusterInfoUseCase = getClusterInfoUseCase;
    this.listNodesUseCase = listNodesUseCase;
  }

  @GetMapping("/info")
  public ResponseEntity<?> getCluster() {
    GetClusterInfoUseCaseOutput output = this.getClusterInfoUseCase.execute();
    return ResponseEntity.ok(output);
  }

  @GetMapping("/nodes")
  public ResponseEntity<?> getNodes() {
    List<ListNodesUseCaseOutput> output = this.listNodesUseCase.execute();
    return ResponseEntity.ok(output);
  }
}
