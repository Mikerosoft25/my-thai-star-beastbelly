-- new statuses:
INSERT INTO ORDERSTATUS (id, modificationCounter, status)
VALUES
	(0, 1, 'Received'),
	(1, 1, 'Preparation'),
	(2, 1, 'Delivered'),
	(3, 1, 'Complete'),
	(4, 1, 'Canceled');

INSERT INTO ORDERPAYSTATUS (id, modificationCounter, payStatus)
VALUES
	(0, 1, 'Pending'),
	(1, 1, 'Paid'),
	(2, 1, 'Canceled');


INSERT INTO Orders (id, modificationCounter, idBooking, idInvitedGuest, idHost, idOrderStatus, idOrderPayStatus)
VALUES
  -- for booking with id 0
	(0, 1, 0, null, null, 0, 0),
	-- for booking with id 1
	(1, 1, 1, null, null, 0, 0),
	-- for booking with id 2
	(2, 1, 2, null, null, 0, 0),
	-- for booking with id 3
	(3, 1, 3, null, null, 0, 0),
  -- for booking with id 4
	(4, 1, 4, null, null, 0, 0),
  -- for booking with id 5
	(5, 1, 5, null, null, 3, 1);


INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder)
VALUES
	-- for order with id 0
	(0, 1, 0, 2, 'please not too spicy', 0),
	(1, 1, 4, 1, null, 0),
	(2, 1, 2, 1, null, 0),
	-- for order with id 1
	(3, 1, 4, 2, null, 1),
	(4, 1, 2, 1, null, 1),
	(5, 1, 3, 1, null, 1),
	-- for order with id 2
	(6, 1, 2, 1, null, 2),
	-- for order with id 3
	(7, 1, 6, 2, null, 3),
	-- for order with id 4
	(8, 1, 1, 3, null, 4),
	-- for order with id 5
	(9, 1, 2, 2, null, 5);

INSERT INTO OrderDishExtraIngredient (id, modificationCounter, idOrderLine, idIngredient)
VALUES
	(0, 1, 0, 0),
	(1, 1, 0, 1),
	(2, 1, 1, 1),
	(3, 1, 3, 1),
	(4, 1, 4, 1),
	(5, 1, 8, 0),
	(6, 1, 9, 0),
	(7, 1, 9, 1);
