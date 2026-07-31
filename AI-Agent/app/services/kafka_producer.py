import json
import uuid
from typing import Dict, Any
from app.config.settings import settings
from app.utils.logger import logger

class AIEventProducer:
    """
    Kafka Producer for streaming AI Service events (careeros.ai.events).
    Emits events whenever ATS scores, resume rewrites, or career roadmaps are generated.
    """

    def __init__(self):
        self.bootstrap_servers = settings.KAFKA_BOOTSTRAP_SERVERS
        self.topic = settings.KAFKA_AUDIT_TOPIC
        self.producer = None

        try:
            from kafka import KafkaProducer
            self.producer = KafkaProducer(
                bootstrap_servers=self.bootstrap_servers.split(","),
                value_serializer=lambda v: json.dumps(v).encode("utf-8"),
                key_serializer=lambda k: k.encode("utf-8") if k else None,
                retries=3
            )
            logger.info(f"Initialized Kafka AI Event Producer targeting servers: {self.bootstrap_servers}")
        except Exception as e:
            logger.warn(f"Kafka Producer initialization skipped ({e}). AI events will log locally.")

    def publish_ai_event(self, event_type: str, user_id: str, payload: Dict[str, Any]):
        event_data = {
            "eventId": str(uuid.uuid4()),
            "eventType": event_type,
            "userId": user_id,
            "payload": payload,
            "timestamp": str(uuid.uuid1())
        }

        if self.producer:
            try:
                self.producer.send(self.topic, key=user_id, value=event_data)
                logger.info(f"Published Kafka AI event '{event_type}' for user {user_id}")
                return
            except Exception as e:
                logger.error(f"Failed to publish Kafka AI event: {e}")

        logger.info(f"AI Event (Local Log): [{event_type}] for user {user_id}: {payload.get('summary', '')}")

ai_event_producer = AIEventProducer()
