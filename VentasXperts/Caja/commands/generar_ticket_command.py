# commands/generar_ticket_command.py
from rest_framework import status
from rest_framework.response import Response
from django.template.loader import render_to_string
import datetime, os, io
from xhtml2pdf import pisa
from django.conf import settings
from Administracion.models import Producto, CarritoProducto
from .ticket_command import TicketCommand

class GenerarTicketCommand(TicketCommand):
    def execute(self, request, *args, **kwargs):
        productos = Producto.objects.all()
        carrito_productos = CarritoProducto.objects.select_related('producto').all()
        total_productos = sum([cp.cantidad for cp in carrito_productos])
        total_costo_productos = sum([cp.subtotal for cp in carrito_productos])
        
        fecha_actual = datetime.datetime.now().strftime("%d/%m/%Y")
        hora_actual = datetime.datetime.now().strftime("%H:%M:%S")

        context = {
            'productos': productos,
            'carritoProductos': carrito_productos,
            'total_productos': total_productos,
            'total_costo_productos': total_costo_productos,
            'fecha_actual': fecha_actual,
            'hora_actual': hora_actual,
            'usuario': request.user
        }

        html = render_to_string('ticket_pdf.html', context)
        pdf_dir = os.path.join(settings.BASE_DIR, 'Caja', 'media', 'pdf_ticket')
        os.makedirs(pdf_dir, exist_ok=True)
        pdf_path = os.path.join(pdf_dir, f'ticket_{datetime.datetime.now().strftime("%Y%m%d%H%M%S")}.pdf')

        with open(pdf_path, "wb") as pdf_file:
            pisa.CreatePDF(io.BytesIO(html.encode("UTF-8")), dest=pdf_file, encoding='UTF-8')

        return Response({'message': 'Ticket generado', 'ticket_path': pdf_path}, status=status.HTTP_201_CREATED)
