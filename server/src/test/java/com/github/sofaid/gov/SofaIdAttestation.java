package com.github.sofaid.gov;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class SofaIdAttestation extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610abe806100206000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063aba6466c1461007d578063b894610d14610115578063ccbab89e146101a2578063d254cd361461021d578063e6e4ffaa14610298578063f9b68b31146102f3575b600080fd5b34801561008957600080fd5b506100be600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061034e565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156101015780820151818401526020810190506100e6565b505050509050019250505060405180910390f35b34801561012157600080fd5b50610160600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061041a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156101ae57600080fd5b50610203600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610467565b604051808215151515815260200191505060405180910390f35b34801561022957600080fd5b5061027e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610496565b604051808215151515815260200191505060405180910390f35b3480156102a457600080fd5b506102d9600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506104c5565b604051808215151515815260200191505060405180910390f35b3480156102ff57600080fd5b50610334600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061085a565b604051808215151515815260200191505060405180910390f35b60606000808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002080548060200260200160405190810160405280929190818152602001828054801561040e57602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190600101908083116103c4575b50505050509050919050565b60006020528160005260406000208181548110151561043557fe5b906000526020600020016000915091509054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60026020528160005260406000206020528060005260406000206000915091509054906101000a900460ff1681565b60016020528160005260406000206020528060005260406000206000915091509054906101000a900460ff1681565b6000809050600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515156105ef576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260378152602001807f546869732070617274792068617320616c72656164792072656365697665642081526020017f616e206174746573746174696f6e2066726f6d20796f7500000000000000000081525060400191505060405180910390fd5b60018060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055506000808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000203390806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506001600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550600190507f37dbfbafb428a834253ac27ef4459b7ef8f6a7d65fbc172186109a7d68a1dbc63383604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390a1919050565b6000809050600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16151561095d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f416c726561647920696e76616c6964206174746573746174696f6e000000000081525060200191505060405180910390fd5b6000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550600190507f2bb6257e0b83e9e940d36bb2472105ba8184c3f2a0df19566f6d1504281e89bf3383604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390a19190505600a165627a7a72305820b01f54da4958811b6514f634cfde1a3b2d8df7fc9ab068796f597cb1e8d71cad0029";

    public static final String FUNC_GETATTESTATIONS = "getAttestations";

    public static final String FUNC_ATTESTEDBY = "attestedBy";

    public static final String FUNC_VALIDATTESTATION = "validAttestation";

    public static final String FUNC_ATTESTEDTO = "attestedTo";

    public static final String FUNC_SIGNATTESTATION = "signAttestation";

    public static final String FUNC_INVALIDATEATTESTATION = "invalidateAttestation";

    public static final Event SIGNATTESTATION_EVENT = new Event("SignAttestation",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event INVALIDATEATTESTATION_EVENT = new Event("InvalidateAttestation",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected SofaIdAttestation(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SofaIdAttestation(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SofaIdAttestation(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SofaIdAttestation(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<List> getAttestations(String _target) {
        final Function function = new Function(FUNC_GETATTESTATIONS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_target)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<String> attestedBy(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_ATTESTEDBY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0),
                        new org.web3j.abi.datatypes.generated.Uint256(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> validAttestation(String param0, String param1) {
        final Function function = new Function(FUNC_VALIDATTESTATION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0),
                        new org.web3j.abi.datatypes.Address(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> attestedTo(String param0, String param1) {
        final Function function = new Function(FUNC_ATTESTEDTO,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0),
                        new org.web3j.abi.datatypes.Address(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> signAttestation(String _target) {
        final Function function = new Function(
                FUNC_SIGNATTESTATION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_target)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> invalidateAttestation(String _target) {
        final Function function = new Function(
                FUNC_INVALIDATEATTESTATION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_target)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<SignAttestationEventResponse> getSignAttestationEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SIGNATTESTATION_EVENT, transactionReceipt);
        ArrayList<SignAttestationEventResponse> responses = new ArrayList<SignAttestationEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SignAttestationEventResponse typedResponse = new SignAttestationEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.signer = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._target = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SignAttestationEventResponse> signAttestationEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, SignAttestationEventResponse>() {
            @Override
            public SignAttestationEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SIGNATTESTATION_EVENT, log);
                SignAttestationEventResponse typedResponse = new SignAttestationEventResponse();
                typedResponse.log = log;
                typedResponse.signer = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._target = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SignAttestationEventResponse> signAttestationEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SIGNATTESTATION_EVENT));
        return signAttestationEventFlowable(filter);
    }

    public List<InvalidateAttestationEventResponse> getInvalidateAttestationEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(INVALIDATEATTESTATION_EVENT, transactionReceipt);
        ArrayList<InvalidateAttestationEventResponse> responses = new ArrayList<InvalidateAttestationEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            InvalidateAttestationEventResponse typedResponse = new InvalidateAttestationEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.signer = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.target = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<InvalidateAttestationEventResponse> invalidateAttestationEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, InvalidateAttestationEventResponse>() {
            @Override
            public InvalidateAttestationEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(INVALIDATEATTESTATION_EVENT, log);
                InvalidateAttestationEventResponse typedResponse = new InvalidateAttestationEventResponse();
                typedResponse.log = log;
                typedResponse.signer = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.target = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<InvalidateAttestationEventResponse> invalidateAttestationEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(INVALIDATEATTESTATION_EVENT));
        return invalidateAttestationEventFlowable(filter);
    }

    @Deprecated
    public static SofaIdAttestation load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SofaIdAttestation(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SofaIdAttestation load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SofaIdAttestation(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SofaIdAttestation load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SofaIdAttestation(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SofaIdAttestation load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SofaIdAttestation(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SofaIdAttestation> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SofaIdAttestation.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SofaIdAttestation> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SofaIdAttestation.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SofaIdAttestation> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SofaIdAttestation.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SofaIdAttestation> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SofaIdAttestation.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class SignAttestationEventResponse {
        public Log log;

        public String signer;

        public String _target;
    }

    public static class InvalidateAttestationEventResponse {
        public Log log;

        public String signer;

        public String target;
    }
}
