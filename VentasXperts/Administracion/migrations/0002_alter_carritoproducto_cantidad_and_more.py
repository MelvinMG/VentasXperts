# Generated by Django 5.1.7 on 2025-03-15 19:42

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Administracion', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='carritoproducto',
            name='cantidad',
            field=models.IntegerField(null=True),
        ),
        migrations.AlterField(
            model_name='carritoproducto',
            name='subtotal',
            field=models.DecimalField(decimal_places=2, max_digits=10, null=True),
        ),
    ]
