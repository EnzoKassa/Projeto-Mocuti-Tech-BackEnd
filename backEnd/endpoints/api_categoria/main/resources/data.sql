-- As instruções DML (insert, update e delete)
-- aqui serão executadas quando a aplicação iniciar
-- PODEM haver quantas forem necessárias.
-- apenas separe elas com ponto e vírgula.

-- MAAAAS, para que funcione, é necessária a seguinte configuração no application.properties:
-- spring.jpa.defer-datasource-initialization=true

INSERT INTO categoria (nome, descricao)
VALUES
('Palestra', 'Palestras tematicas com power point'),
('Doação', 'Evento de doação de roupas e/ou brinquedos'),
('Carnaval', 'Comemoração carnaval'),
('Apadrinhamento', 'Doação de brinquedos e roupas para crianças');
