MTProto-akka-demo
================

Write a primitive *MTProto* server using scodec and akka-stream (TCP).

The server must be able to perform the first 2 commands when initializing a DH session: *req_pq* and *req_DH_params*.

On *req_pq*, it gives *res_pq* with random data and waits for *req_DH_params* and then closes the connection.

Validation is not needed, use *random* data and ignore rules in the docs regarding fields' values. The result should be a demonstration of __akka stream + scodec__.

For TL String serialization __scodec.codecs.ascii32__ should be used. All numeric codecs are big-endian.

The cipher for *req_DH_params* should be __RSA/ECB/NoPadding__.

Documentation for mtproto:

* https://core.telegram.org/mtproto/auth_key

* https://core.telegram.org/mtproto/description#unencrypted-message