# login-service

- POST `/user/register`

  - request body:
    ```json
    {
		"username": "john",
		"password": "John123@",
		"email": "john@mail.com"
	}
    ```
  - response body [202 Accepted]:
    ```json
    {
	    "provisional_id": "d3544e82-0208-4391-b829-11dd6b34f113",
    	"email": "john@mail.com"
	}
    ```

---

- POST `/user/validate`

  - request body:
    ```json
    {
	    "provisional_id": "d3544e82-0208-4391-b829-11dd6b34f113",
    	"activation_code": "805256"
	}
    ```
  - response body [201 Created]:
    ```json
    {
		"user_id": 2,
		"username": "john",
		"email": "john@mail.com",
		"roles": [
			"CUSTOMER"
		]
	}
    ```

---

- GET `/user/validate`

  - request params:
    - `provisional_id`: UUID (e.g. `d3544e82-0208-4391-b829-11dd6b34f113`)
	- `activation_code`: String (e.g. `805256`)
  - response body [201 Created]:
    ```json
    {
		"user_id": 4,
		"username": "frank",
		"email": "frank@mail.com",
		"roles": [
			"CUSTOMER"
		]
	}
    ```

---

- POST `/user/login`

  - request body:
    ```json
    {
		"username": "john",
		"password": "John123@" 
	}
    ```
  - response body [200 OK]:
    ```json
    {
	    "authorization": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.
		eyJzdWIiOiJqb2huIiwiaWF0IjoxNjU4Nzk2NTY0LCJleHAiOjE2NTg4MDAxNjQsInJvbGVzIjpbIkNVU1RPTUVSIl19.  
		iLUYNYwm8Q9noAHl602_WZT3Xd7BWBZ0GAA3TBELbT8"
	}
    ```

---

- POST `/user/changePassword`

  - request body:
    ```json
    {
		"old_password": "John123@", 
		"new_password": "@321nhoJ"
	}
    ```
  - response body [200 OK]: none

---

- DELETE `/user/deleteAccount`

  - request body: none
  - response body [200 OK]: none

---

- POST `/admin/enrolling`

  - request body:
    ```json
    {
		"username": "admin213",
		"password": "Admin213@",
		"enrolling_capability": 0
	}
    ```
  - response body [201 Created]:
    ```json
    {
		"user_id": 3,
		"username": "admin213",
		"enrolling_capability": 0,
		"roles": [
			"ADMIN"
		]
	}
    ```

---

- GET `/admin/disableAccount/{userId}`

  - response body [200 OK]: none

---

- POST `/turnstile/register`

  - request body:
    ```json
    {
		"secret": "Secret123@"
	}
    ```
  - response body [201 Created]:
    ```json
    {
		"id": 1
	}
    ```

---

- POST `/turnstile/token`

  - request body:
    ```json
    {
		"id": "1",
		"secret": "Secret123@"
	}
    ```
  - response body [200 OK]:
    ```json
    {
		"authorization": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU5MDQyNzU5LCJleHAiOjE2NTkwNDYzNTksInJvbGVzIjpbIkVNQkVEREVEIl19.wSBhcaAQMErLpSL882Ij3ZvNJNeYEJ4GrTKZe0G46lA"
	}
    ```

---

# traveler-service

- GET `/my/profile`

  - response body [200 OK]:
    ```json
    {
		"username": "john",
		"roles": [
			"CUSTOMER"
		],
		"date_of_birth": "01/01/1970",
		"address": "address 123",
		"telephone_number": "0123456789"
	}
    ```

---

- PUT `/my/profile`

  - request body:
    ```json
    {
		"date_of_birth": "01/01/2000",
		"address": "new address 321",
		"telephone_number": "9876543210"
	}
    ```
  - response body [200 OK]:
    ```json
    {
		"username": "john",
		"roles": [
			"CUSTOMER"
		],
		"date_of_birth": "01/01/2000",
		"address": "new address 321",
		"telephone_number": "9876543210"
	}
    ```

---

- POST `/my/tickets`

  - request body:
    ```json
    {
		"cmd": "buy_tickets",
		"quantity": 1,
		"zones": "ABC"	
	}
    ```
  - response body [200 OK]:
    ```json
    {
        "sub": 1,
        "iat": "2022-07-26T02:11:53.549+00:00",
        "exp": "2022-07-26T03:11:53.549+00:00",
        "zid": "ABC",
        "jws": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.  
		eyJzdWIiOiIxIiwiaWF0IjoxNjU4ODAxNTEzLCJleHAiOjE2NTg4MDUxMTMsInppZCI6IkFCQyJ9.  
		InBgJnA9IGrF7hrBzb3GSasIqHLFVbOWquaCFomSINc",
        "qrcode": "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEAAQAAAAB0CZXLAAACvklEQVR4Xu2W0WrlMAxEDf4tg35d4N8yuHPktLdNYfdlO2whoimJfS4M0kh223+Jdl+4xwOceIATD3DifwFWay0ysu+5V98Ze881WDQCsdcY9a9n1xYP306Az4m8MVqsgOZ1mAE+5tJ6zOzZOplyA6yraJSoVb3cAJ8xQUblCc0sOgEJG9/i5ur79j8GKuQXqZNf9EzF+7oJWBQp9ky1MMr0oopdkAmQwEvTXGpcITRRO4suoDoVXYlTqoskeGYYgX2cyvfcyk92HPwyrQNYDPJVRqn/lE8a6xcuQCnRKkPsjJHDfyrWzwMnSbOONQqESk3S/pEoA8ASu2itBk5cw64RSGbWKIWVsGycsFag2lUPPctuB5NdXsUyAIFV0BpyrUCJxC4UzgVwpKl9j13LOvr+UqyfB5iecUnFt51hTrqcgN7lUkQqVcqPJpmy1kqnCaA6zK5knEqwJC9k4iEfwEpnkLSqWa9yxcswBiDrHDsqj2GUJTUz4QJUH3mF8bnIlXpmIjucwLnVJJcLiiWJkSxAuACKpep07ItgIFnWCqhtuOQoVzjl9G+c3nUBS6cHpVGFZBU9nQNOFx0joI4ZlaRqGmDRDPRwAigkS4g8li21TgDPBhc9TZGsl66iTSug42TTtHSQtFWCVCyWbcDmfgVEkqStVKLXByyU1XHGYcIBT+I01vitCWB6V3UW8mJxuPf6mRFQRtZlmFmHutRefrEBE6vSPvqbWEdZWp+raQBkDjllMzjqbCnHKF9G4BRIkyOjGLbFabD5AEJjSxVTjfTSq3tEGQEpkyLsspmfEwd9GaQOIPCs3ErrLNqF+1bJ9gHqlTGuMjUSVQdbf00YE6CWrQESWHhTrJtIA/AhslWudMGofRsQxXCr0TjltiEkBUK4AEw7aNhqFSqlXm5HpQn4YzzAiQc48QAnfgXwBnPQJpdOYUCdAAAAAElFTkSuQmCC",
		"used": false
    }
    ```

---

- GET `/my/tickets`

  - response body [200 OK]:
    ```json
    {
        "sub": 1,
        "iat": "2022-07-26T02:11:53.549+00:00",
        "exp": "2022-07-26T03:11:53.549+00:00",
        "zid": "ABC",
        "jws": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.  
		eyJzdWIiOiIxIiwiaWF0IjoxNjU4ODAxNTEzLCJleHAiOjE2NTg4MDUxMTMsInppZCI6IkFCQyJ9.  
		InBgJnA9IGrF7hrBzb3GSasIqHLFVbOWquaCFomSINc",
        "qrcode": "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEAAQAAAAB0CZXLAAACvklEQVR4Xu2W0WrlMAxEDf4tg35d4N8yuHPktLdNYfdlO2whoimJfS4M0kh223+Jdl+4xwOceIATD3DifwFWay0ysu+5V98Ze881WDQCsdcY9a9n1xYP306Az4m8MVqsgOZ1mAE+5tJ6zOzZOplyA6yraJSoVb3cAJ8xQUblCc0sOgEJG9/i5ur79j8GKuQXqZNf9EzF+7oJWBQp9ky1MMr0oopdkAmQwEvTXGpcITRRO4suoDoVXYlTqoskeGYYgX2cyvfcyk92HPwyrQNYDPJVRqn/lE8a6xcuQCnRKkPsjJHDfyrWzwMnSbOONQqESk3S/pEoA8ASu2itBk5cw64RSGbWKIWVsGycsFag2lUPPctuB5NdXsUyAIFV0BpyrUCJxC4UzgVwpKl9j13LOvr+UqyfB5iecUnFt51hTrqcgN7lUkQqVcqPJpmy1kqnCaA6zK5knEqwJC9k4iEfwEpnkLSqWa9yxcswBiDrHDsqj2GUJTUz4QJUH3mF8bnIlXpmIjucwLnVJJcLiiWJkSxAuACKpep07ItgIFnWCqhtuOQoVzjl9G+c3nUBS6cHpVGFZBU9nQNOFx0joI4ZlaRqGmDRDPRwAigkS4g8li21TgDPBhc9TZGsl66iTSug42TTtHSQtFWCVCyWbcDmfgVEkqStVKLXByyU1XHGYcIBT+I01vitCWB6V3UW8mJxuPf6mRFQRtZlmFmHutRefrEBE6vSPvqbWEdZWp+raQBkDjllMzjqbCnHKF9G4BRIkyOjGLbFabD5AEJjSxVTjfTSq3tEGQEpkyLsspmfEwd9GaQOIPCs3ErrLNqF+1bJ9gHqlTGuMjUSVQdbf00YE6CWrQESWHhTrJtIA/AhslWudMGofRsQxXCr0TjltiEkBUK4AEw7aNhqFSqlXm5HpQn4YzzAiQc48QAnfgXwBnPQJpdOYUCdAAAAAElFTkSuQmCC",
		"used": false
    }
    ```

---

- GET `/admin/travelers`

  - response body [200 OK]:
    ```json
    [
		{
			"username": "jim",
			"roles": [
				"CUSTOMER"
			],
			"date_of_birth": "31/12/1970",
			"address": "address 321",
			"telephone_number": "1231231231"
		},
		{
			"username": "john",
			"roles": [
				"CUSTOMER"
			],
			"date_of_birth": "01/01/1970",
			"address": "address 123",
			"telephone_number": "0123456789"
		}
	]
    ```

---

- GET `/admin/traveler/{userId}/profile`

  - response body [200 OK]:
    ```json
    {
		"username": "john",
		"roles": [
			"CUSTOMER"
		],
		"date_of_birth": "01/01/1970",
		"address": "address 123",
		"telephone_number": "0123456789"
	}
    ```

---

- GET `/admin/traveler/{userId}/tickets`

  - response body [200 OK]:
    ```json
    {
        "sub": 1,
        "iat": "2022-07-26T02:11:53.549+00:00",
        "exp": "2022-07-26T03:11:53.549+00:00",
        "zid": "ABC",
        "jws": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.  
		eyJzdWIiOiIxIiwiaWF0IjoxNjU4ODAxNTEzLCJleHAiOjE2NTg4MDUxMTMsInppZCI6IkFCQyJ9.  
		InBgJnA9IGrF7hrBzb3GSasIqHLFVbOWquaCFomSINc",
        "qrcode": "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEAAQAAAAB0CZXLAAACvklEQVR4Xu2W0WrlMAxEDf4tg35d4N8yuHPktLdNYfdlO2whoimJfS4M0kh223+Jdl+4xwOceIATD3DifwFWay0ysu+5V98Ze881WDQCsdcY9a9n1xYP306Az4m8MVqsgOZ1mAE+5tJ6zOzZOplyA6yraJSoVb3cAJ8xQUblCc0sOgEJG9/i5ur79j8GKuQXqZNf9EzF+7oJWBQp9ky1MMr0oopdkAmQwEvTXGpcITRRO4suoDoVXYlTqoskeGYYgX2cyvfcyk92HPwyrQNYDPJVRqn/lE8a6xcuQCnRKkPsjJHDfyrWzwMnSbOONQqESk3S/pEoA8ASu2itBk5cw64RSGbWKIWVsGycsFag2lUPPctuB5NdXsUyAIFV0BpyrUCJxC4UzgVwpKl9j13LOvr+UqyfB5iecUnFt51hTrqcgN7lUkQqVcqPJpmy1kqnCaA6zK5knEqwJC9k4iEfwEpnkLSqWa9yxcswBiDrHDsqj2GUJTUz4QJUH3mF8bnIlXpmIjucwLnVJJcLiiWJkSxAuACKpep07ItgIFnWCqhtuOQoVzjl9G+c3nUBS6cHpVGFZBU9nQNOFx0joI4ZlaRqGmDRDPRwAigkS4g8li21TgDPBhc9TZGsl66iTSug42TTtHSQtFWCVCyWbcDmfgVEkqStVKLXByyU1XHGYcIBT+I01vitCWB6V3UW8mJxuPf6mRFQRtZlmFmHutRefrEBE6vSPvqbWEdZWp+raQBkDjllMzjqbCnHKF9G4BRIkyOjGLbFabD5AEJjSxVTjfTSq3tEGQEpkyLsspmfEwd9GaQOIPCs3ErrLNqF+1bJ9gHqlTGuMjUSVQdbf00YE6CWrQESWHhTrJtIA/AhslWudMGofRsQxXCr0TjltiEkBUK4AEw7aNhqFSqlXm5HpQn4YzzAiQc48QAnfgXwBnPQJpdOYUCdAAAAAElFTkSuQmCC",
		"used": false
    }
    ```

---

- PUT `/turnstile/validate/{ticketId}`

  - response body [200 OK]:
    ```json
    true
    ```

---

# ticket-catalogue-service

- GET `/tickets`

  - response body [200 OK]:
    ```json
	{
		"id": 1,
		"type": "ORDINAL",
		"validity_zones": "ABC",
		"price": 2.5,
		"min_age": null,
		"max_age": 30
	}
	{
		"id": 2,
		"type": "WEEKEND",
		"validity_zones": "CDE",
		"price": 2.0,
		"min_age": 18,
		"max_age": null
	}
	{
		"id": 3,
		"type": "WEEKEND",
		"validity_zones": "B",
		"price": 2.0,
		"min_age": 18,
		"max_age": 30
	}
    ```

---

- POST `/shop/{ticketId}`

  - request body:
    ```json
	{
		"ticket_id": 1,
		"tickets_quantity": 3,
		"card_holder": "John Doe",
		"card_number": "1234567890123456",
		"card_exp_date": "10/10",
		"card_cvv": "123"
	}
    ```
  - response body [200 OK]:
    ```json
	{
		"orderId": 4
	}
    ```

---

- GET `/orders`

  - response body [200 OK]:
    ```json
	{
		"id": 1,
		"ticket_id": 1,
		"quantity": 100,
		"status": "PENDING",
		"username": "john"
	}
	{
		"id": 3,
		"ticket_id": 3,
		"quantity": 10,
		"status": "COMPLETED",
		"username": "john"
	}
    ```

	---

- GET `/orders/{orderId}`

  - response body [200 OK]:
    ```json
	{
		"id": 1,
		"ticket_id": 1,
		"quantity": 100,
		"status": "PENDING",
		"username": "john"
	}
    ```

---

- POST `/admin/tickets`

  - request body:
    ```json
	{
		"type": "ORDINAL",
		"validity_zones": "BC",
		"price": 10.0,
		"min_age": 18,
		"max_age": null
	}
    ```
  - response body [200 OK]:
    ```json
	{
		"id": 4,
		"type": "ORDINAL",
		"validity_zones": "BC",
		"price": 10.0,
		"min_age": 18,
		"max_age": null
	}
    ```

---

- GET `/admin/orders`

  - response body [200 OK]:
    ```json
	{
		"id": 1,
		"ticket_id": 1,
		"quantity": 100,
		"status": "PENDING",
		"username": "john"
	}
	{
		"id": 2,
		"ticket_id": 2,
		"quantity": 50,
		"status": "COMPLETED",
		"username": "jim"
	}
	{
		"id": 4,
		"ticket_id": 1,
		"quantity": 3,
		"status": "PENDING",
		"username": "john"
	}
    ```

---

- GET `/admin/orders/{userId}`

  - response body [200 OK]:
    ```json
	{
		"id": 2,
		"ticket_id": 2,
		"quantity": 50,
		"status": "COMPLETED",
		"username": "jim"
	}
    ```

---

- GET `/admin/orders/date`

  - request params:
    - `start`: Date (e.g. `2022-01-01`)
	- `end`: Date (e.g. `2022-12-31`)
  - response body [200 OK]:
    ```json
	{
		"id": 1,
		"ticket_id": 1,
		"quantity": 100,
		"status": "PENDING",
		"username": "john",
		"date_time": "2022-07-28T14:51:13.293+00:00"
	}
	{
		"id": 2,
		"ticket_id": 2,
		"quantity": 50,
		"status": "COMPLETED",
		"username": "jimmy",
		"date_time": "2022-06-22T17:10:25.000+00:00"
	}
	{
		"id": 3,
		"ticket_id": 3,
		"quantity": 10,
		"status": "COMPLETED",
		"username": "john",
		"date_time": "2022-06-14T22:00:00.000+00:00"
	}
    ```

---

- GET `/admin/orders/{userId}/date`

  - request params:
    - `start`: Date (e.g. `2022-01-01`)
	- `end`: Date (e.g. `2022-12-31`)
  - response body [200 OK]:
    ```json
	{
		"id": 2,
		"ticket_id": 2,
		"quantity": 50,
		"status": "COMPLETED",
		"username": "jimmy",
		"date_time": "2022-06-22T17:10:25.000+00:00"
	}
    ```

---

# payment-service

- GET `/transactions`

  - response body [200 OK]:
    ```json
	{
		"order_id": 4,
		"order_status": "COMPLETED",
		"username": "john",
		"total_cost": 7.5
	}
	{
		"order_id": 5,
		"order_status": "COMPLETED",
		"username": "john",
		"total_cost": 20.0
	}
	```

---

- GET `/admin/transactions`

  - response body [200 OK]:
    ```json
	{
		"order_id": 4,
		"order_status": "COMPLETED",
		"username": "john",
		"total_cost": 7.5
	}
	{
		"order_id": 5,
		"order_status": "COMPLETED",
		"username": "john",
		"total_cost": 20.0
	}
	{
		"order_id": 6,
		"order_status": "COMPLETED",
		"username": "jimmy",
		"total_cost": 30.0
	}
	```

---
---
