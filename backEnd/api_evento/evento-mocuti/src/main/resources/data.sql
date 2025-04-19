INSERT INTO evento (
  id_evento, nome_evento, descricao, dia, hora_inicio, hora_fim, is_aberto, qtd_vaga, qtd_interessado,
   foto, fk_endereco_evento, fk_status_evento, fk_publico_alvo_evento, fk_categoria_evento
) VALUES (
    DEFAULT, 'Feira de Tecnologia', 'Evento com exibição de projetos de inovação e palestras sobre o futuro da TI.',
    '2025-05-20', '14:00:00', '18:00:00', 0, 200, NULL, NULL, 1, 2, 3, 4
);