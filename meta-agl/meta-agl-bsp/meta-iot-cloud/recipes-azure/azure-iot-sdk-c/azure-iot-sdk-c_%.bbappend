# Disable the amqp transport, due to linking error in meta-iot repository for amqp
# symbols from amqp code are not included in the libiothub_client.so

# NOTE: amqp is not used by AGL in IoT context at the moment
PACKAGECONFIG_remove = "amqp"
