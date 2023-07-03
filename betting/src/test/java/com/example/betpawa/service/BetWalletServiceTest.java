package com.example.betpawa.service;

import com.betpawa.wallet.WalletInteractionServiceGrpc;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class BetWalletServiceTest {

    @InjectMocks
    private BetWalletService betWalletService;
//    @Mock
//    private WalletInteractionServiceGrpc.WalletInteractionServiceBlockingStub walletStub;
//    @Mock
//    private WalletInteractionServiceGrpc.WalletInteractionServiceStub walletStubAsync;

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private final WalletInteractionServiceGrpc.WalletInteractionServiceImplBase serviceImpl =
            mock(WalletInteractionServiceGrpc.WalletInteractionServiceImplBase.class, delegatesTo(
                    new WalletInteractionServiceGrpc.WalletInteractionServiceImplBase() {
                        // By default the client will receive Status.UNIMPLEMENTED for all RPCs.
                        // You might need to implement necessary behaviors for your test here, like this:
                        //
                        // @Override
                        // public void sayHello(HelloRequest request, StreamObserver<HelloReply> respObserver) {
                        //   respObserver.onNext(HelloReply.getDefaultInstance());
                        //   respObserver.onCompleted();
                        // }
                    }));

//    @Before
//    public void setUp() throws Exception {
//        // Generate a unique in-process server name.
//        String serverName = InProcessServerBuilder.generateName();
//
//        // Create a server, add service, start, and register for automatic graceful shutdown.
//        grpcCleanup.register(InProcessServerBuilder
//                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
//
//        // Create a client channel and register for automatic graceful shutdown.
//        ManagedChannel channel = grpcCleanup.register(
//                InProcessChannelBuilder.forName(serverName).directExecutor().build());
//
//        // Create a HelloWorldClient using the in-process channel;
////        client = new HelloWorldClient(channel);
//    }
//
//    @Test
//    void shouldPlaceBet() {
//        WalletInteractionServiceGrpc.WalletInteractionServiceBlockingStub chatService = mock(WalletInteractionServiceGrpc.WalletInteractionServiceBlockingStub.class);
//        AmountResponse response = AmountResponse.newBuilder().build();
//
////        when(walletStub.reserveMoney(any(AmountRequest.class)))
////                .thenReturn(response);
//
//        betWalletService.placeBet(1L, BigDecimal.TEN);
//
////        verify(walletStub).reserveMoney(any());
////
////
////        String eMail = RandomStringUtils.randomAlphanumeric(10) + "@gmail.com";
////        Account account = Account.builder().email(eMail).amount(BigDecimal.TEN).build();
////        CompletableFuture<Long> result = controller.createAccount(account);
////        Long id = result.get();
////
////        assertThat(id).isNotNull();
////
////        AccountDto dto = controller.getAccountById(id).get();
////
////        assertThat(dto.getEmail()).isEqualTo(eMail);
//    }
//
}
