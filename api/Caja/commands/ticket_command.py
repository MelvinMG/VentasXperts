# Caja/commands/ticket_command.py
from abc import ABC, abstractmethod

class TicketCommand(ABC):
    @abstractmethod
    def execute(self, request, *args, **kwargs):
        pass
