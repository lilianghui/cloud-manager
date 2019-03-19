SELECT
	T2.*,
	T1.lvl
FROM
	(
		SELECT
			@r AS _id,
			( SELECT @r := parent_batch_id FROM cloudbatch.t_nimble_batch_base WHERE id = _id ) AS parent_id,
			@l := @l + 1 AS lvl
		FROM
			( SELECT @r := 'd7982950-e535-4b31-b555-42ab1e18f91a', @l := 0 ) vars,
			cloudbatch.t_nimble_batch_base h
		WHERE @r IS NOT NULL
	) T1
	JOIN cloudbatch.t_nimble_batch_base T2 ON T1._id = T2.id
	ORDER BY T1.lvl DESC;