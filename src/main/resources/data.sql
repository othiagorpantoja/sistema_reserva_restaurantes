-- Inserção de dados iniciais para mesas
INSERT INTO tables (id, capacity, is_active, location) VALUES 
('T001', 2, true, 'Área interna'),
('T002', 2, true, 'Área interna'),
('T003', 4, true, 'Área interna'),
('T004', 4, true, 'Área interna'),
('T005', 4, true, 'Área interna'),
('T006', 6, true, 'Área interna'),
('T007', 6, true, 'Área interna'),
('T008', 8, true, 'Área externa'),
('T009', 8, true, 'Área externa'),
('T010', 10, true, 'Área externa'),
('T011', 12, true, 'Área VIP'),
('T012', 2, false, 'Área interna - Manutenção');

-- Inserção de dados de exemplo para reservas (opcional)
-- INSERT INTO reservations (id, table_id, customer_name, customer_email, customer_phone, special_requests, reservation_date_time, duration_minutes, number_of_people, status, created_at, updated_at) VALUES
-- ('R001', 'T001', 'João Silva', 'joao@email.com', '(11) 99999-9999', 'Mesa próxima à janela', '2024-12-25 19:00:00', 120, 2, 'CONFIRMED', NOW(), NOW()),
-- ('R002', 'T003', 'Maria Santos', 'maria@email.com', '(11) 88888-8888', '', '2024-12-25 20:00:00', 120, 4, 'PENDING', NOW(), NOW());
