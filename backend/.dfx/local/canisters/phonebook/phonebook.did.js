export default ({ IDL }) => {
  const Name = IDL.Text;
  const Phone = IDL.Nat;
  const Entry = IDL.Record({
    'name' : Name,
    'description' : IDL.Text,
    'phone' : Phone,
  });
  return IDL.Service({
    'lookup' : IDL.Func([Name], [IDL.Opt(Entry)], ['query']),
    'insert' : IDL.Func([Name, IDL.Text, Phone], [], []),
  });
};