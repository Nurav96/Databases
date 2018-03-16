--Rooms and Rates
SELECT *, poop FROM lab6_rooms
WHERE RoomCode IN (
SELECT Room as poop FROM lab6_reservations
WHERE Room = "HBB" 
GROUP BY Room

select * FROM lab6_reservations
WHERE (CheckIn >= DATE_SUB(NOW(), INTERVAL 180 day) OR CheckOut >= DATE_SUB(NOW(), INTERVAL 180 day))
AND (CheckIn < NOW() OR CheckOut < NOW())
AND Room = "HBB";

--Reservations

--Detailed Reservation Information

--Revenue