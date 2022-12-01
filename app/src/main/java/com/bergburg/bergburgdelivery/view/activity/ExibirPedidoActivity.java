package com.bergburg.bergburgdelivery.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.ViewPedidoAdapter;
import com.bergburg.bergburgdelivery.databinding.ActivityExibirPedidoBinding;
import com.bergburg.bergburgdelivery.helpers.Permissoes;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.ItensPedido;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Status_pedido;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.viewmodel.ExibirPedidoViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExibirPedidoActivity extends AppCompatActivity {
    private ActivityExibirPedidoBinding binding;
    private ExibirPedidoViewModel viewModel;
    private ViewPedidoAdapter adapter ;
    private DadosPreferences preferences;
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;
    private  Dialog dialogInternet;
    private Usuario usuarioAtual = new Usuario();
    private Boolean primeiroCarregamento = true;
    private  Menu menu;

    private Dialog dialog;
    private ProgressBar progressBarStatus;

    private Endereco enderecoLocal = new Endereco();
    private Pedido pedidoLocal = new Pedido();
    private List<ItensPedido> itensPedidosLocal = new ArrayList<>();

    private String[] permissoes = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExibirPedidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = new DadosPreferences(this);
        viewModel = new ViewModelProvider(this).get(ExibirPedidoViewModel.class);

        setSupportActionBar(binding.toolbarPersonalizada.toolbar);

        dialog = new Dialog(this);

        //validar Permissao
        Permissoes.validarPermissoes(permissoes, this, 1);

        binding.toolbarPersonalizada.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new ViewPedidoAdapter(ExibirPedidoActivity.this);



        binding.recyclerviewViewDelhatesPedido.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerviewViewDelhatesPedido.setAdapter(adapter);


        observe();
    }

    private void startClock(){
        final Calendar calendar = Calendar.getInstance();
        this.runnable = new Runnable() {
            @Override
            public void run() {
                if(!ticker){
                    return;
                }

                            calendar.setTimeInMillis(System.currentTimeMillis());
                            System.out.println("ExibirPedido -Milisegundos: "+System.currentTimeMillis());

                            Handler handler = new Handler();
                            Bundle bundle = getIntent().getExtras() ;
                            if(bundle != null){
                                Long idPedido = bundle.getLong(Constantes.ID_PEDIDO);
                                System.out.println("idPedido: "+idPedido);
                                viewModel.listarPedido(idPedido);
                                Long idUsuario = preferences.recuperarID();
                                   if(idUsuario != null){
                                       if(idUsuario == Constantes.ADMIN){
                                            viewModel.visualizarPedido(idPedido);
                                       }
                                   }
                            }


                            Long now = SystemClock.uptimeMillis();
                            Long next = now + (1000 - (now % 1000));
                            handler.postAtTime(runnable,next);

            }
        };
        this.runnable.run();
    }

    private void observe() {
        viewModel.endereco.observe(this, new Observer<Endereco>() {
            @Override
            public void onChanged(Endereco endereco) {
                enderecoLocal = endereco;

                adapter.attackEnderede(endereco);
            }
        });
        viewModel.pedido.observe(this, new Observer<Pedido>() {
            @Override
            public void onChanged(Pedido pedido) {
                pedidoLocal = pedido;
                System.out.println("listarPedido: "+pedido.toString());
                viewModel.getStatusPedido(pedido.getId());
                viewModel.getItensPedido(pedido.getId());
                viewModel.getUsuario(pedido.getIdUsuario());
                viewModel.buscarEnderecoSalvo(pedido.getIdUsuario());
                adapter.attackPedido(pedido);
            }
        });
        viewModel.itensPedido.observe(this, new Observer<List<ItensPedido>>() {
            @Override
            public void onChanged(List<ItensPedido> itensPedidos) {
                if(itensPedidosLocal.size() > 0){
                    itensPedidosLocal.clear();
                }
                     itensPedidosLocal = itensPedidos;

                System.out.println("itensPedidos: "+itensPedidos.size());
                adapter.attackItensPedidos(itensPedidos);
            }
        });
        viewModel.statusPedido.observe(this, new Observer<List<Status_pedido>>() {
            @Override
            public void onChanged(List<Status_pedido> status_pedidos) {
                System.out.println("status_pedidos: "+status_pedidos.size());
                adapter.attackstatusPedido(status_pedidos);
            }
        });
        viewModel.usuario.observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
               if(usuario != null){
                   usuarioAtual = usuario;
                   System.out.println("usuario id: "+usuario.toString());
                   adapter.attackUsuario(usuario);
               }
            }
        });

        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    dialog.dismiss();
                }
                progressBarStatus.setVisibility(View.GONE);
                Toast.makeText(ExibirPedidoActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void criarPDFEntrega(){
        if (ContextCompat.checkSelfPermission(ExibirPedidoActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ExibirPedidoActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


            System.out.println(pedidoLocal.toString());
            System.out.println("Total de itens: " + itensPedidosLocal.size());
           // LinearLayout layoutPedido = binding.linearLayoutPedido;
           // Bitmap bitmap, bitmapLogo;
            int pageWidth = 210;
            int espacamentoDefault = 270;
            int espacamento_unitarioDefault = 280; // VALOR UNITARIO DOS ITENS
            int espaco = 20;
            int altura_da_pagina = 297;
            int totalItens = 0;
            int tamanhoObservacaoString = 224;
            float totalPedido = 0f;
            String observacaoFormatada = "";
            int caracteresPorLinha = 52;
            //calculo
            if (itensPedidosLocal != null) {
                totalItens = itensPedidosLocal.size();
                altura_da_pagina = (espacamento_unitarioDefault + (totalItens * espaco)) + 120;  // lagura da pagina sem observações
                // lagura da pagina com observações
                for (ItensPedido item : itensPedidosLocal) {
                    if (!item.getObservacao().isEmpty()) {

                        observacaoFormatada += "" + System.lineSeparator() +
                                "☞ " + item.getTitulo() + System.lineSeparator() +
                                "" +
                                item.getObservacao().toLowerCase(Locale.ROOT).replaceAll("\n", " ") + System.lineSeparator() +
                                "";

                        if (item.getObservacao().length() > 200) {
                            altura_da_pagina += 60;
                        } else if (item.getObservacao().length() > 170 && item.getObservacao().length() <= 200) {
                            altura_da_pagina += 60;
                        } else if (item.getObservacao().length() > 120 && item.getObservacao().length() <= 170) {
                            altura_da_pagina += 60;
                        } else if (item.getObservacao().length() > 90 && item.getObservacao().length() <= 120) {
                            altura_da_pagina += 35;
                        } else if (item.getObservacao().length() > 60 && item.getObservacao().length() <= 90) {
                            altura_da_pagina += 30;
                        } else if (item.getObservacao().length() > 30 && item.getObservacao().length() <= 60) {
                            altura_da_pagina += 30;
                        } else if (item.getObservacao().length() > 10 && item.getObservacao().length() <= 30) {
                            altura_da_pagina += 30;
                        } else if (item.getObservacao().length() <= 10) {
                            altura_da_pagina += 15;
                        }

                    }


                }

                System.out.println("Tamanho: " + altura_da_pagina);
            }

            try { //PDF PARA ENTREGA

        /*    Bitmap bitmap = Bitmap.createBitmap(layoutPedido.getWidth(),layoutPedido.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            layoutPedido.draw(canvas);*/


                //  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logoburgs);
                // bitmapLogo = Bitmap.createScaledBitmap(bitmap,100,100,false);

                PdfDocument mypdfDocument = new PdfDocument();
                Paint paint = new Paint(); //297
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(pageWidth, altura_da_pagina, 1).create();
                PdfDocument.Page page = mypdfDocument.startPage(myPageInfo);

                Canvas canvas = page.getCanvas();

                //  canvasPDF.drawBitmap(bitmap,0,0,paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                //     paint.setTextSize(3f);
                canvas.drawText("BERGS BURG DELIVERY", pageWidth / 2, 20, paint);

                // canvas.drawBitmap(bitmapLogo,0,10,paint);
                String endereco = "Rua Principal do Panorama XXI, Q. Dois, 01 - Mangueirão";
                 endereco = preferences.recuperarEnderecoLoja();
                String enderecoPart1 = endereco.substring(0,endereco.length()/2);
                String enderecoPart2 = endereco.substring(endereco.length()/2,endereco.length());

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                paint.setTextSize(8f);
              //  canvas.drawText("Rua Principal do Panorama XXI,", pageWidth / 2, 35, paint);
                canvas.drawText(enderecoPart1, pageWidth / 2, 35, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                paint.setTextSize(8f);
               // canvas.drawText(" Q. Dois, 01  Mangueirão, Belém - PA", pageWidth / 2, 45, paint);
                canvas.drawText(enderecoPart2, pageWidth / 2, 45, paint);

                String telefone = "00000000000";
                telefone = preferences.recuperarTelefone();
                String dd = telefone.substring(0,2);
                String numero = telefone.substring(2);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
               // canvas.drawText("(91) 98235-9645", pageWidth / 2, 57, paint);
                canvas.drawText("("+dd+") "+numero, pageWidth / 2, 57, paint);
                //PDF PARA ENTREGA
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("Data", pageWidth / 2, 70, paint);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("" + pedidoLocal.getData_pedido(), pageWidth / 2, 80, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, 90, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("***** NÃO É DOCUMENTO FISCAL *****", pageWidth / 2, 100, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Pedido N° ", 10, 120, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("" + pedidoLocal.getId(), 60, 120, paint);

                //PDF PARA ENTREGA
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, 130, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("Dados do cliente", pageWidth / 2, 140, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Nome:", 10, 153, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText(usuarioAtual.getNome(), 42, 153, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Telefone:", 10, 163, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText(usuarioAtual.getTelefone(), 53, 163, paint);


                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("Endereço de entrega", pageWidth / 2, 183, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Bairro:", 10, 195, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText(enderecoLocal.getBairro(), 42, 195, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Rua:", 10, 205, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText(enderecoLocal.getRua() +" - "+enderecoLocal.getNumeroCasa(), 30, 205, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Cidade:", 10, 215, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText(enderecoLocal.getCidade(), 45, 215, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText(enderecoLocal.getComplemento(), 10, 225, paint);
                //PDF PARA ENTREGA

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, 240, paint);



               paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("#", 10, 255, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Item Qtd.", 20, 255, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setTextSize(8f);
                canvas.drawText("Total", 200, 255, paint);

                //PDF PARA ENTREGA
                for (ItensPedido item : itensPedidosLocal) {
                    int index = (itensPedidosLocal.indexOf(item) + 1);

                    if (index <= 10) {

                        Float total = item.getQuantidade() * item.getPreco();
                        totalPedido += total;
                        // item
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText("" + index, 10, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getTitulo(), 20, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.RIGHT);
                        paint.setTextSize(8f);
                        canvas.drawText("R$ " + String.format("%.2f", total), 200, espacamentoDefault, paint);
                        // valor unitario
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getQuantidade() + " x $" + String.format("%.2f", item.getPreco()), 20, espacamento_unitarioDefault, paint);
                        espacamentoDefault += espaco;
                        espacamento_unitarioDefault += espaco;

                    } else if (index > 10 && index <= 99) {

                        Float total = item.getQuantidade() * item.getPreco();
                        totalPedido += total;
                        // item
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText("" + index, 10, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getTitulo(), 25, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.RIGHT);
                        paint.setTextSize(8f);
                        canvas.drawText("R$ " + String.format("%.2f", total), 200, espacamentoDefault, paint);
                        // valor unitario
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getQuantidade() + " x $" + String.format("%.2f", item.getPreco()), 20, espacamento_unitarioDefault, paint);
                        espacamentoDefault += espaco;
                        espacamento_unitarioDefault += espaco;

                    } else if (index > 99) {

                        Float total = item.getQuantidade() * item.getPreco();
                        totalPedido += total;
                        // item
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText("" + index, 10, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getTitulo(), 30, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.RIGHT);
                        paint.setTextSize(8f);
                        canvas.drawText("R$ " + String.format("%.2f", total), 200, espacamentoDefault, paint);
                        // valor unitario
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getQuantidade() + " x $" + String.format("%.2f", item.getPreco()), 20, espacamento_unitarioDefault, paint);
                        espacamentoDefault += espaco;
                        espacamento_unitarioDefault += espaco;

                    }


                }
                //PDF PARA ENTREGA

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(9f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, espacamento_unitarioDefault - 5, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("SubTotal", 10, (espacamento_unitarioDefault + 10), paint);

                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setTextSize(8f);
                canvas.drawText("R$ " + String.format("%.2f", (pedidoLocal.getTotal() - pedidoLocal.getTaxa_entrega())), 200, (espacamento_unitarioDefault + 10), paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Taxa de entrega", 10, (espacamento_unitarioDefault + 20), paint);

                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setTextSize(8f);
                canvas.drawText("R$ " + String.format("%.2f", pedidoLocal.getTaxa_entrega()), 200, (espacamento_unitarioDefault + 20), paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Total", 10, (espacamento_unitarioDefault + 30), paint);

                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setTextSize(8f);
                canvas.drawText("R$ " + String.format("%.2f", (pedidoLocal.getTotal())), 200, (espacamento_unitarioDefault + 30), paint);



                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(9f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, espacamento_unitarioDefault +50, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Forma de pagamento: "+pedidoLocal.getFormaDePagamento(), 10, (espacamento_unitarioDefault + 64), paint);



                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(9f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, espacamento_unitarioDefault +74, paint);

                //PDF PARA ENTREGA

                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize(8);
                StaticLayout staticLayout;
                Boolean tem_observacao = false;


                for (ItensPedido item : itensPedidosLocal) {
                    if (!item.getObservacao().isEmpty()) {
                        if (tem_observacao == false) {
                            paint.setTextAlign(Paint.Align.LEFT);
                            paint.setTextSize(8f);
                            canvas.drawText("Observações:", 10, (espacamento_unitarioDefault + 84), paint);
                            tem_observacao = true;
                        }
                    }
                }
                //PDF PARA ENTREGA

                staticLayout = StaticLayout.Builder
                        .obtain(observacaoFormatada, 0, observacaoFormatada.length(), textPaint, canvas.getWidth() - 10)
                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                        .build();
                canvas.translate(10, espacamento_unitarioDefault + 84);
                staticLayout.draw(canvas);


                File direct = new File(Environment.getExternalStorageDirectory()+"/Pedidos_delivery");

                if(!direct.exists()) {
                    if(direct.mkdir()); //se não existir o diretorio e criado
                }

                System.out.println("TAMANHO " +altura_da_pagina);


                //PDF PARA ENTREGA

                //Compartilhar o pdf

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                mypdfDocument.finishPage(page);
                File file = new File(direct, "/Pedido_" + pedidoLocal.getId() + ".pdf");
                System.out.println("Caminho: " + file.getAbsolutePath());
                System.out.println("Caminho: " + file.getPath());

                mypdfDocument.writeTo(new FileOutputStream(file));
                System.out.println("Sucesso");

                final Uri arquivo = Uri.fromFile(file);
                final Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, arquivo);
                intent.putExtra(Intent.EXTRA_TITLE, "Imprimir pedido");

                intent.setType("application/pdf");

                startActivity(Intent.createChooser(intent, "Selecionar impressora"));

                mypdfDocument.close();
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }
        }else{
            askPermission();
        }
    }
    //TEVE QUE CRIAR DOIS DEVIDO A QUANTIDADE DE ESPAÇO PARA CADA PDF
    private void criarPDFRetirarNOLocal(){
        if (ContextCompat.checkSelfPermission(ExibirPedidoActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ExibirPedidoActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


            System.out.println(pedidoLocal.toString());
            System.out.println("Total de itens: " + itensPedidosLocal.size());
            // LinearLayout layoutPedido = binding.linearLayoutPedido;
            // Bitmap bitmap, bitmapLogo;
            int pageWidth = 210;
            int espacamentoDefault = 245;
            int espacamento_unitarioDefault = 255; // VALOR UNITARIO DOS ITENS
            int espaco = 20;
            int altura_da_pagina = 297;
            int totalItens = 0;
            int tamanhoObservacaoString = 224;
            float totalPedido = 0f;
            String observacaoFormatada = "";
            int caracteresPorLinha = 52;
            //calculo
            if (itensPedidosLocal != null) {
                totalItens = itensPedidosLocal.size();
                altura_da_pagina = (espacamento_unitarioDefault + (totalItens * espaco)) + 120;  // lagura da pagina sem observações
                // lagura da pagina com observações
                for (ItensPedido item : itensPedidosLocal) {
                    if (!item.getObservacao().isEmpty()) {

                        observacaoFormatada += "" + System.lineSeparator() +
                                "☞ " + item.getTitulo() + System.lineSeparator() +
                                "" +
                                item.getObservacao().toLowerCase(Locale.ROOT).replaceAll("\n", " ") + System.lineSeparator() +
                                "";

                        if (item.getObservacao().length() > 200) {
                            altura_da_pagina += 60;
                        } else if (item.getObservacao().length() > 170 && item.getObservacao().length() <= 200) {
                            altura_da_pagina += 60;
                        } else if (item.getObservacao().length() > 120 && item.getObservacao().length() <= 170) {
                            altura_da_pagina += 60;
                        } else if (item.getObservacao().length() > 90 && item.getObservacao().length() <= 120) {
                            altura_da_pagina += 35;
                        } else if (item.getObservacao().length() > 60 && item.getObservacao().length() <= 90) {
                            altura_da_pagina += 30;
                        } else if (item.getObservacao().length() > 30 && item.getObservacao().length() <= 60) {
                            altura_da_pagina += 30;
                        } else if (item.getObservacao().length() > 10 && item.getObservacao().length() <= 30) {
                            altura_da_pagina += 30;
                        } else if (item.getObservacao().length() <= 10) {
                            altura_da_pagina += 15;
                        }

                    }


                }

                System.out.println("Tamanho: " + altura_da_pagina);
            }

            try {

        /*    Bitmap bitmap = Bitmap.createBitmap(layoutPedido.getWidth(),layoutPedido.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            layoutPedido.draw(canvas);*/


                //  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logoburgs);
                // bitmapLogo = Bitmap.createScaledBitmap(bitmap,100,100,false);

                PdfDocument mypdfDocument = new PdfDocument();
                Paint paint = new Paint(); //297
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(pageWidth, altura_da_pagina, 1).create();
                PdfDocument.Page page = mypdfDocument.startPage(myPageInfo);

                Canvas canvas = page.getCanvas();

                //  canvasPDF.drawBitmap(bitmap,0,0,paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                //     paint.setTextSize(3f);
                canvas.drawText("BERGS BURG DELIVERY", pageWidth / 2, 20, paint);

                // canvas.drawBitmap(bitmapLogo,0,10,paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                paint.setTextSize(8f);
                canvas.drawText("Rua Principal do Panorama XXI,", pageWidth / 2, 35, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                paint.setTextSize(8f);
                canvas.drawText(" Q. Dois, 01  Mangueirão, Belém - PA", pageWidth / 2, 45, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("(91) 98235-9645", pageWidth / 2, 57, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("Data", pageWidth / 2, 70, paint);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("" + pedidoLocal.getData_pedido(), pageWidth / 2, 80, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, 90, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("***** NÃO É DOCUMENTO FISCAL *****", pageWidth / 2, 100, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Pedido N° ", 10, 120, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("" + pedidoLocal.getId(), 60, 120, paint);


                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, 130, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("Dados do cliente", pageWidth / 2, 140, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Nome:", 10, 153, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText(usuarioAtual.getNome(), 42, 153, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Telefone:", 10, 166, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText(usuarioAtual.getTelefone(), 53, 166, paint);


                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("Opção de entrega", pageWidth / 2, 187, paint);



                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Retirar no local.", 10, 205, paint);



                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(8f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, 220, paint);



                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("#", 10, 230, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Item Qtd.", 20, 230, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setTextSize(8f);
                canvas.drawText("Total", 200, 230, paint);


                for (ItensPedido item : itensPedidosLocal) {
                    int index = (itensPedidosLocal.indexOf(item) + 1);

                    if (index <= 10) {

                        Float total = item.getQuantidade() * item.getPreco();
                        totalPedido += total;
                        // item
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText("" + index, 10, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getTitulo(), 20, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.RIGHT);
                        paint.setTextSize(8f);
                        canvas.drawText("R$ " + String.format("%.2f", total), 200, espacamentoDefault, paint);
                        // valor unitario
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getQuantidade() + " x $" + String.format("%.2f", item.getPreco()), 20, espacamento_unitarioDefault, paint);
                        espacamentoDefault += espaco;
                        espacamento_unitarioDefault += espaco;

                    } else if (index >= 10 && index <= 99) {

                        Float total = item.getQuantidade() * item.getPreco();
                        totalPedido += total;
                        // item
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText("" + index, 9, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(" "+item.getTitulo(), 25, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.RIGHT);
                        paint.setTextSize(8f);
                        canvas.drawText("R$ " + String.format("%.2f", total), 200, espacamentoDefault, paint);
                        // valor unitario
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getQuantidade() + " x $" + String.format("%.2f", item.getPreco()), 20, espacamento_unitarioDefault, paint);
                        espacamentoDefault += espaco;
                        espacamento_unitarioDefault += espaco;

                    } else if (index > 99) {

                        Float total = item.getQuantidade() * item.getPreco();
                        totalPedido += total;
                        // item
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText("" + index, 10, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getTitulo(), 30, espacamentoDefault, paint);
                        paint.setTextAlign(Paint.Align.RIGHT);
                        paint.setTextSize(8f);
                        canvas.drawText("R$ " + String.format("%.2f", total), 200, espacamentoDefault, paint);
                        // valor unitario
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(8f);
                        canvas.drawText(item.getQuantidade() + " x $" + String.format("%.2f", item.getPreco()), 20, espacamento_unitarioDefault, paint);
                        espacamentoDefault += espaco;
                        espacamento_unitarioDefault += espaco;

                    }


                }


                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(9f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, espacamento_unitarioDefault - 8, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("SubTotal", 10, (espacamento_unitarioDefault + 5), paint);

                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setTextSize(8f);
                canvas.drawText("R$ " + String.format("%.2f", (pedidoLocal.getTotal())), 200, (espacamento_unitarioDefault + 5), paint);



                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Total", 10, (espacamento_unitarioDefault + 15), paint);

                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setTextSize(8f);
                canvas.drawText("R$ " + String.format("%.2f", (pedidoLocal.getTotal())), 200, (espacamento_unitarioDefault + 15), paint);



                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(9f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, espacamento_unitarioDefault +25, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8f);
                canvas.drawText("Forma de pagamento: "+pedidoLocal.getFormaDePagamento(), 10, (espacamento_unitarioDefault + 36), paint);



                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(9f);
                canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", pageWidth / 2, espacamento_unitarioDefault +46, paint);



                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize(8);
                StaticLayout staticLayout;
                Boolean tem_observacao = false;


                for (ItensPedido item : itensPedidosLocal) {
                    if (!item.getObservacao().isEmpty()) {
                        if (tem_observacao == false) {
                            paint.setTextAlign(Paint.Align.LEFT);
                            paint.setTextSize(8f);
                            canvas.drawText("Observações:", 10, (espacamento_unitarioDefault + 55), paint);
                            tem_observacao = true;
                        }
                    }
                }


                staticLayout = StaticLayout.Builder
                        .obtain(observacaoFormatada, 0, observacaoFormatada.length(), textPaint, canvas.getWidth() - 10)
                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                        .build();
                canvas.translate(10, espacamento_unitarioDefault + 55);
                staticLayout.draw(canvas);


                File direct = new File(Environment.getExternalStorageDirectory()+"/Pedidos_delivery");

                if(!direct.exists()) {
                    if(direct.mkdir()); //se não existir o diretorio e criado
                }

                System.out.println("TAMANHO " +altura_da_pagina);




                //Compartilhar o pdf

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                mypdfDocument.finishPage(page);
                File file = new File(direct, "/Pedido_" + pedidoLocal.getId() + ".pdf");
                System.out.println("Caminho: " + file.getAbsolutePath());
                System.out.println("Caminho: " + file.getPath());

                mypdfDocument.writeTo(new FileOutputStream(file));
                System.out.println("Sucesso");

                final Uri arquivo = Uri.fromFile(file);
                final Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, arquivo);
                intent.putExtra(Intent.EXTRA_TITLE, "Imprimir pedido");

                intent.setType("application/pdf");

                startActivity(Intent.createChooser(intent, "Selecionar impressora"));

                mypdfDocument.close();
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }
        }else{
            askPermission();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pedido,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        Long idUsuario = preferences.recuperarID();
        if(idUsuario != null){
            if(idUsuario == Constantes.ADMIN){
                menu.setGroupVisible(R.id.groupAdmin,true);
            }else{
                menu.setGroupVisible(R.id.groupAdmin,false);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.chatPedido:
                String nome = "";
                if(usuarioAtual != null){
                    if(usuarioAtual.getId() != null){

                        if(usuarioAtual.getId() != Constantes.ADMIN){
                            nome = usuarioAtual.getNome();
                        }else{
                            nome = "Chat";
                        }
                        System.out.println(usuarioAtual.toString());
                        Bundle bundle = new Bundle();
                        bundle.putString(Constantes.NOME,nome);
                        bundle.putLong(Constantes.ID_USUARIO,usuarioAtual.getId());
                        bundle.putLong(Constantes.ID_CONVERSA,usuarioAtual.getIdConversa());
                        Intent intent = new Intent(ExibirPedidoActivity.this,ChatActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                }
             //   deslogar();
                break;
            case R.id.login:
               /// startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
            case R.id.impremirPedido:

                if(pedidoLocal.getOpcaoEntrega().equalsIgnoreCase(Constantes.RETIRAR_N0_LOCAL)){
                    criarPDFRetirarNOLocal();
                }else{
                    criarPDFEntrega();
                }


                break;

            case R.id.alterarStatus:
                opcaos_de_pedido(pedidoLocal);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void askPermission() {

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

    }

    private void opcaos_de_pedido(Pedido pedido){
        dialog.setContentView(R.layout.layout_opcoes_pedido);

        Spinner spinnerStatus = dialog.findViewById(R.id.spinnerStatus);
        progressBarStatus = dialog.findViewById(R.id.progressBarStatus);
        Button btnSalvar = dialog.findViewById(R.id.buttonSalvar);
        String[] status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExibirPedidoActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,status);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarStatus.setVisibility(View.VISIBLE);
                new AlertDialog.Builder(ExibirPedidoActivity.this)
                        .setCancelable(false)
                        .setTitle("Alteração")
                        .setMessage("Alterar o status para ("+spinnerStatus.getSelectedItem().toString()+")" )
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.salvarStatusPedido(pedido.getId(),spinnerStatus.getSelectedItem().toString());

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBarStatus.setVisibility(View.GONE);
                            }
                        }).show();

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                // snackbar("Permissão de localização não aceita!");
                //  criarPDF();
                askPermission();
                System.out.println("Sem Permissao");

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticker = true;
        startClock();
        primeiroCarregamento = true;
        itensPedidosLocal.clear();



    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public void onStop() {
        super.onStop();
        ticker = false;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ticker = false;


    }
}