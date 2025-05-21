-- As instruções DML (insert, update e delete)
-- aqui serão executadas quando a aplicação iniciar
-- PODEM haver quantas forem necessárias.
-- apenas separe elas com ponto e vírgula.

-- MAAAAS, para que funcione, é necessária a seguinte configuração no application.properties:
-- spring.jpa.defer-datasource-initialization=true

INSERT INTO musica
VALUES
('2025-03-17', DEFAULT, true, 0, 'CANTOR X', 'BLA BLA',null),
('2025-03-17', DEFAULT, false, 0, 'CANTOR Z', 'proibidao',null);
