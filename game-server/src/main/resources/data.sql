INSERT INTO login2.roles (id,`role`) VALUES
	 (1,'ADMIN'),
	 (2,'USER');

INSERT INTO login2.user_roles (user_id,id) VALUES
     	 (0,2),
     	 (1,1);